package data;

import black.door.hate.HalResource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import models.*;
import org.javatuples.Pair;
import play.Logger;
import play.Play;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toConcurrentMap;

/**
 * Created by nfischer on 3/3/2016.
 */
@Getter
public enum LolUThoughtThereWasGoingToBeADatabase {
	INST;

	private static final URL indexUrl;

	static {
		try {
			indexUrl = new URL(Play.application().configuration().getString("index_url"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static final ObjectMapper MAPPER = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory())
			.registerModule(new JavaTimeModule())
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	private static final Duration REFRESH_INTERVAL =
			Duration.ofMinutes(Play.application().configuration()
					.getLong("refresh_interval"));
	private Instant lastRefresh;

	private Map<String, Person> people = new ConcurrentHashMap<>();

	private Map<String, Map<Long, ? extends HalResource>>
			everythingElse = new ConcurrentHashMap<>();

	public static LolUThoughtThereWasGoingToBeADatabase instance(){
		if(INST.lastRefresh == null ||
				INST.lastRefresh.plus(REFRESH_INTERVAL).isBefore(Instant.now())){
			synchronized (INST){
				updatePeople();
			}
		}
		return INST;
	}

	private static Function<Pair<String, Future<HttpResponse<InputStream>>>, Optional<Person>>
			redeemFutures = t -> {
		try {
			Person p = YAML_MAPPER.readValue(
					t.getValue1().get(10, TimeUnit.SECONDS)
							.getBody(), Person.class);
			return Optional.of(p);
		} catch (InterruptedException |
				ExecutionException |
				TimeoutException |
				IOException e) {
			Logger.error(e.getMessage());
			Logger.error("Could not retrieve or parse data from " + t.getValue0());
			return Optional.empty();
		}
	};

	private static void updatePeople(){
		try {
			HttpResponse<InputStream> indexResult = Unirest.get(indexUrl.toString())
					.asBinary();
			if(300 > indexResult.getStatus() && indexResult.getStatus() <= 200){
				List<String> urls = YAML_MAPPER.readValue(indexResult.getBody(),
						new TypeReference<List<String>>(){});
				INST.people = urls.parallelStream()
						.map(url -> Pair.with(url, Unirest.get(url).asBinaryAsync()))
						.map(redeemFutures)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.collect(toConcurrentMap(Person::getHandle, identity()));
				updateResources();
			}else{
				Logger.error("Bad response from index retrieval " + indexResult.getStatus());
			}
		} catch (UnirestException | IOException e) {
			Logger.error("Unable to retrieve index from " + indexUrl);
		}
		INST.lastRefresh = Instant.now();
	}

	private static void updateResources(){
		Map<String, Person> peeps = INST.getPeople();

		Map<Long, ? extends Contact> contacts = peeps.values().parallelStream()
				.flatMap(p -> p.getContactInfo().parallelStream())
				.collect(toConcurrentMap(Base::getId, identity()));
		INST.everythingElse.put("contact", contacts);

		Map<Long, Job> experiences = peeps.values().parallelStream()
				.flatMap(p -> p.getWorkExperience().parallelStream())
				.collect(toConcurrentMap(Base::getId, identity()));
		INST.everythingElse.put("jobs", experiences);

		Map<Long, School> educations = peeps.values().parallelStream()
				.flatMap(p -> p.getEducation().parallelStream())
				.collect(toConcurrentMap(Base::getId, identity()));
		INST.everythingElse.put("schools", educations);

		Map<Long, Project> projects = peeps.values().parallelStream()
				.flatMap(p -> p.getProjects().parallelStream())
				.collect(toConcurrentMap(Base::getId, identity()));
		INST.everythingElse.put("projects", projects);
	}
}

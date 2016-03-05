package controllers;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.Person;
import play.mvc.Controller;
import play.mvc.Result;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import static data.LolUThoughtThereWasGoingToBeADatabase.MAPPER;
import static data.LolUThoughtThereWasGoingToBeADatabase.instance;
import static black.door.hate.Constants.HAL_JSON_CONTENT_TYPE;

public class EverythingController extends Controller{

    public Result retrieve(String id) throws JsonProcessingException {
	    Person p = instance()
			    .getPeople()
			    .get(id);
	    if(p == null)
		    return notFound();

	    JsonNode n = MAPPER.valueToTree(p.asEmbedded());
        return ok(n)
		        .as(HAL_JSON_CONTENT_TYPE);
    }

	public Result listPeople() throws URISyntaxException, JsonProcessingException {
		Collection<Person> people = instance().getPeople().values();
		HalRepresentation rep = HalRepresentation.builder()
				.addLink("self", new URI("/people"))
				.addLink("people", people)
				.addProperty("count", people.size())
				.build();
		return ok(rep.serialize()).as(HAL_JSON_CONTENT_TYPE);
	}

	public Result retrieveIdResource(String resource, long id) throws JsonProcessingException {
		Map<Long, ?extends HalResource> resources = instance().getEverythingElse().get(resource);
		if(resources == null)
			return notFound("no " + resource + " to be found here.");
		HalResource r = resources.get(id);
		if(r == null)
			return notFound();
		JsonNode n = MAPPER.valueToTree(r.asEmbedded());
		return ok(n).as(HAL_JSON_CONTENT_TYPE);
	}

}

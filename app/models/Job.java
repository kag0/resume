package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URI;
import java.time.Period;
import java.time.YearMonth;

import static data.LolUThoughtThereWasGoingToBeADatabase.MAPPER;

/**
 * Created by nfischer on 2/29/2016.
 */
@Data
public class Job extends Base implements HalResource{

	private String title;
	private String company;
	private Location location;
	private String description;
	private YearMonth start;
	private YearMonth end = YearMonth.now();

	public Period getDuration(){
		return Period.between(start.atEndOfMonth(), end.atEndOfMonth());
	}

	@Override
	public HalRepresentation.HalRepresentationBuilder representationBuilder() {
		return HalRepresentation.builder()
				.ignoreNullProperties(true)
				.ignoreNullResources(true)
				.addLink("self", this)
				.addProperties(MAPPER.valueToTree(this));
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/jobs/" +getId());
	}
}

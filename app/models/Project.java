package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URI;
import java.time.YearMonth;

import static data.LolUThoughtThereWasGoingToBeADatabase.MAPPER;

/**
 * Created by nfischer on 2/29/2016.
 */
@Data
public class Project extends Base implements HalResource{
	private String name;
	private YearMonth date;
	private URI site;
	private String description;

	@Override
	public HalRepresentation.HalRepresentationBuilder representationBuilder() {
		return HalRepresentation.builder()
				.addLink("self", this)
				.addProperties(MAPPER.valueToTree(this));
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/projects/" + getId());
	}
}

package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URI;
import java.time.YearMonth;

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
		val b = HalRepresentation.builder()
				.ignoreNullProperties(true)
				.ignoreNullResources(true)
				.addLink("self", this)
				.addProperty("date", date)
				.addProperty("description", description);
		if(site != null)
			b.addLink("site", site);
		return b;
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/projects/" + getId());
	}
}

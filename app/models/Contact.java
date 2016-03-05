package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URI;

/**
 * Created by nfischer on 2/29/2016.
 */
@Data
public class Contact extends Base implements HalResource{
	private String type;
	private String value;
	private URI link;

	@Override
	public HalRepresentation.HalRepresentationBuilder representationBuilder() {
		val b = HalRepresentation.builder()
				.ignoreNullProperties(true)
				.ignoreNullResources(true)
				.addLink("self", this)
				.addProperty("type", getType())
				.addProperty("value", getValue());
		if(link != null)
			b.addLink("link", link);
		return b;
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/contact/" + this.getId());
	}
}

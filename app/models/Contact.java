package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URI;

/**
 * Created by nfischer on 2/29/2016.
 */
@Data
public class Contact extends Base implements HalResource{
	private String type;
	private String value;

	@Override
	public HalRepresentation.HalRepresentationBuilder representationBuilder() {
		return HalRepresentation.builder()
				.addLink("self", this)
				.addProperty("type", getType())
				.addProperty("value", getValue());
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/contact/" + this.getId());
	}
}

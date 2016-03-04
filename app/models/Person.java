package models;

import black.door.hate.HalRepresentation;
import black.door.hate.HalResource;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.List;

/**
 * Created by nfischer on 2/29/2016.
 */
@Data
public class Person implements HalResource{
	private String handle;
	private String name;
	private URI resume;
	private String summary;
	private List<String> interests;
	private List<Contact> contactInfo;
	private Location currentResidence;
	private List<Job> workExperience;
	private List<School> education;
	private List<Project> projects;
	private List<String> skills;

	@Override
	public HalRepresentation.HalRepresentationBuilder representationBuilder() {
		return HalRepresentation.builder()
				.addLink("self", this)
				.addEmbedded("contact", getContactInfo())
				.addLink("experience", getWorkExperience())
				.addLink("education", getEducation())
				.addLink("projects", getProjects())
				.addProperty("name", getName())
				.addProperty("summary", getSummary())
				.addProperty("location", getCurrentResidence())
				.addProperty("skills", getSkills())
				.addProperty("interests", getInterests());
	}

	@Override
	@SneakyThrows
	public URI location() {
		return new URI("/people/" + getHandle());
	}
}

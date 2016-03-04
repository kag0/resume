package controllers;

import black.door.hate.HalResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import play.*;
import play.mvc.*;
import util.ContentType;

import java.util.Map;

import static data.LolUThoughtThereWasGoingToBeADatabase.instance;

public class EverythingController extends Controller implements ContentType {

    public Result retrieve(String id) throws JsonProcessingException {
        return ok(instance().getPeople().get(id).asEmbedded().serialize()).as(HAL_JSON);
    }

	public Result retrieveIdResource(String resource, long id) throws JsonProcessingException {
		Map<Long, ?extends HalResource> resources = instance().getEverythingElse().get(resource);
		if(resources == null)
			return notFound("no " + resource + " to be found here.");
		HalResource r = resources.get(id);
		if(r == null)
			return notFound();

		return ok(r.asEmbedded().serialize()).as(HAL_JSON);
	}

}

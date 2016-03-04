package models;

import java.security.SecureRandom;
import java.util.Random;
import lombok.Data;

/**
 * Created by nfischer on 3/1/2016.
 */

@Data
public abstract class Base {
	public static final Random RANDOM = new SecureRandom();

	protected final long id = RANDOM.nextLong();
}

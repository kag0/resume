package models;

import lombok.Data;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by nfischer on 3/1/2016.
 */

@Data
public abstract class Base {
	public static final Random RANDOM = new SecureRandom();

	protected final long id = abs(RANDOM.nextLong());
}

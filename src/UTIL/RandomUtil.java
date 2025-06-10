package src.UTIL;

import java.util.Random;

public class RandomUtil {
	private Random random;

	public RandomUtil() {
		this.random = new Random();
	}

	/**
	 * Devolve um número uniforme no intervalo [0, 1).
	 */
	public double getUniform() {
		return random.nextDouble();
	}

	/**
	 * Devolve um número segundo uma distribuição exponencial com média aMean.
	 * Usamos: X = -ln(1 - U) / λ, onde λ = 1 / aMean
	 */
	public double getExponential(double aMean) {
		double u = getUniform();
		return -Math.log(1 - u) * aMean;
	}

	/**
	 * Permite fixar a seed para resultados reprodutíveis.
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}
}

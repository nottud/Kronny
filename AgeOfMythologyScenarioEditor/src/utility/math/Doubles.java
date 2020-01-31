package utility.math;

public class Doubles {

	/**
	 * Rounds the provided value to the provided interval.
	 * 
	 * @param value
	 *            The value to round.
	 * @param interval
	 *            The interval to round to.
	 * @return The rounded value to the provided interval.
	 */
	public static double roundToInterval(double value, double interval) {
		return Math.round(value / interval) * interval;
	}

	/**
	 * Floors the provided value to the provided interval.
	 * 
	 * @param value
	 *            The value to floor.
	 * @param interval
	 *            The interval to floor to.
	 * @return The floored value to the provided interval.
	 */
	public static double floorToInterval(double value, double interval) {
		return Math.floor(value / interval) * interval;
	}

	/**
	 * Ceils the provided value to the provided interval.
	 * 
	 * @param value
	 *            The value to ceil.
	 * @param interval
	 *            The interval to floor to.
	 * @return The ceiled value to the provided interval.
	 */
	public static double ceilToInterval(double value, double interval) {
		return Math.ceil(value / interval) * interval;
	}

	/**
	 * Provides the java Modulo operation % but ensures the result is positive.
	 * 
	 * @param value
	 *            The value to apply on.
	 * @param ceiling
	 *            The ceiling value the value will not reach or exceed.
	 * @return The calculated result which will be positive.
	 */
	public static double positiveModulo(double value, double ceiling) {
		double returnValue = value % ceiling;
		if (returnValue < 0) {
			returnValue = returnValue + ceiling;
		}
		return returnValue;
	}

}

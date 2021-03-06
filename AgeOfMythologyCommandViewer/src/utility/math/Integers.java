
package utility.math;

public class Integers {
   
   /**
    * Clamps the input value to between the given minimum and maximum values.
    * i.e. if the input is outside the range, the output will be the boundary value.
    * If the input is within the range, the output will be the same as the input.
    * @param input the input value.
    * @param min the minimum value to clamp to.
    * @param max the maximum value to clamp to.
    * @return the clamped output value.
    */
   public static int clamp(int input, int min, int max) {
      return Math.max(Math.min(input, max), min);
   }
   
}

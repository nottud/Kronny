
package utility.graphics.validationinput;

import java.util.Optional;

/**
 * Provides conversion of {@link String} to a {@link Double} and vice versa.
 */
public class ValidationInputParserDouble implements ValidationInputParser<Double> {
   
   @Override
   public Optional<Double> parse(String value) {
      if (value.isEmpty()) {
         return Optional.of(0.0);
      }
      try {
         return Optional.of(Double.parseDouble(value));
      } catch (NumberFormatException exception) {
         return Optional.empty();
      }
   }
   
   @Override
   public String convertToString(Double value) {
      return value.toString();
   }
   
}


package utility.graphics.validationinput;

import java.util.Optional;

/**
 * Provides conversion of {@link String} to a {@link Float} and vice versa.
 */
public class ValidationInputParserFloat implements ValidationInputParser<Float> {
   
   @Override
   public Optional<Float> parse(String value) {
      if (value.isEmpty()) {
         return Optional.of(0F);
      }
      try {
         return Optional.of(Float.parseFloat(value));
      } catch (NumberFormatException exception) {
         return Optional.empty();
      }
   }
   
   @Override
   public String convertToString(Float value) {
      return value == null ? "" : value.toString();
   }
   
}

package utility.graphics.validationinput;

import java.util.Optional;

/**
 * Provides conversion of {@link String} to a {@link Integer} and vice versa.
 */
public class ValidationInputParserInteger implements ValidationInputParser<Integer> {
   
   @Override
   public Optional<Integer> parse(String value) {
      try {
         return Optional.of(Integer.parseInt(value));
      } catch (NumberFormatException exception) {
         return Optional.empty();
      }
   }
   
   @Override
   public String convertToString(Integer value) {
      return value.toString();
   }
   
}

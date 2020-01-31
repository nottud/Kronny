
package utility.graphics.validationinput;

import java.util.Optional;

/**
 * Provides conversion of {@link String} to a {@link String} and vice versa.
 */
public class ValidationInputParserString implements ValidationInputParser<String> {
   
   @Override
   public Optional<String> parse(String value) {
      return Optional.of(value);
   }
   
   @Override
   public String convertToString(String value) {
      return value;
   }
   
}

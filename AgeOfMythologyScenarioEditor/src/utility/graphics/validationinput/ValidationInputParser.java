
package utility.graphics.validationinput;

import java.util.Optional;

/**
 * Provides a parser that attempts to convert a {@link String} representation into an object it is supposed to represent and back. In the case of
 * converting from a {@link String} it is allowed to reject if the {@link String} is seen as invalid.
 * @param <T> The type of the external object type the parser is trying to create from a {@link String}.
 */
public interface ValidationInputParser<T> {
   
   /**
    * Attempts to turn the entered text into the object it represents. If the {@link String} is invalid then the returned {@link Optional} will be
    * empty.
    * @param value The {@link String} to parse.
    * @return {@link Optional} containing the result if it was successful.
    */
   public Optional<T> parse(String value);
   
   /**
    * Converts the value into its {@link String} representation.
    * @param value Value to get the {@link String} representation from.
    * @return {@link String} representation of the value.
    */
   public String convertToString(T value);
   
}

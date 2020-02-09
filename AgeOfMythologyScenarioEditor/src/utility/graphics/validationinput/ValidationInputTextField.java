
package utility.graphics.validationinput;

import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.TextField;
import utility.graphics.BlockListener;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * Provides editing of an object by the use of an input text box. Entered text will be automatically converted to the associated value as the user
 * types. If the entered text is invalid the previous value will be used and the input box will change colour to show that the entered text is
 * invalid.
 * @param <T> The type of value the input is creating.
 */
public class ValidationInputTextField<T> implements Observable {
   
   protected static final String INVALID_INPUT_STYLE = "-fx-background-color: rgb(255, 150, 150);";
   protected static final String VALID_INPUT_STYLE = "";
   private ValidationInputParser<T> parser;
   private TextField textField;
   private ObservableManager observableManager;
   private ObserverType<T> valueChangedObserverType;
   
   private BlockListener blockListener;
   
   private T lastValidValue;
   
   /**
    * Constructor building the graphics with the initial value. This consider a null input as being invalid.
    * @param initialValue The value to store and display initially.
    * @param parser The parser to use for attempting to convert between a {@link String} and a value and vice versa.
    */
   public ValidationInputTextField(T initialValue, ValidationInputParser<T> parser) {
      this.parser = parser;
      textField = new TextField();
      if (initialValue != null) {
         textField.setText(parser.convertToString(initialValue));
      }
      lastValidValue = initialValue;
      observableManager = new ObservableManagerImpl();
      valueChangedObserverType = new ObserverType<>();
      
      blockListener = new BlockListener();
      
      textField.textProperty().addListener((source, oldValue, newValue) -> handleTextChanged());
   }
   
   /**
    * Handles the text being changed which will attempt to construct the new value and set/remove highlighting depending if the value is valid. If the
    * text is seen to be valid then the value is updated and observers will be notified.
    */
   private void handleTextChanged() {
      if (!blockListener.isBlocked()) {
         Optional<T> conversionAttempt = parser.parse(textField.getText());
         if (conversionAttempt.isPresent()) {
            textField.setStyle(VALID_INPUT_STYLE);
            if (!Objects.equals(conversionAttempt.orElse(lastValidValue), lastValidValue)) {
               updateValueAndNotify(conversionAttempt.orElse(lastValidValue));
            }
         } else {
            if (!textField.getStyle().equals(INVALID_INPUT_STYLE)) {
               textField.setStyle(INVALID_INPUT_STYLE);
            }
         }
      }
   }
   
   /**
    * Updates the last valid value to the provided value and notifies that the value has changed.
    * @param value The value to update to and notify.
    */
   private void updateValueAndNotify(T value) {
      lastValidValue = value;
      observableManager.notifyObservers(valueChangedObserverType, value);
   }
   
   public T getValue() {
      return lastValidValue;
   }
   
   /**
    * Updates to the provided value, changes the display text and assumes it is valid and notifies observers that the value has changed.
    * @param value Value to update to.
    */
   public void setValue(T value) {
      blockListener.setBlocked(true);
      textField.setText(parser.convertToString(value));
      textField.setStyle(VALID_INPUT_STYLE);
      blockListener.setBlocked(false);
      updateValueAndNotify(value);
   }
   
   public TextField getNode() {
      return textField;
   }
   
   public ObserverType<T> getValueChangedObserverType() {
      return valueChangedObserverType;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}

package datahandler.converter;

import java.util.ArrayList;
import java.util.List;

public class FullStringConverter implements DataConverter<String> {
	
	private DataConverter<List<Character>> internalConverter;
	
	public FullStringConverter() {
		internalConverter = new ListConverter<>(new FullCharacterConverter());
	}

	@Override
	public String fromBytes(List<Byte> bytes, int offset) {
		List<Character> characters = internalConverter.fromBytes(bytes, offset);
		StringBuilder stringBuilder = new StringBuilder();
		for(Character character : characters) {
			stringBuilder.append(character);
		}
		return stringBuilder.toString();
	}
	
	@Override
	public List<Byte> toBytes(String value) {
		List<Character> characters = new ArrayList<>(value.length());
		for(char character : value.toCharArray()) {
			characters.add(character);
		}
		return internalConverter.toBytes(characters);
	}
	
	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return internalConverter.getStorageLength(bytes, offset);
	}

	@Override
	public String createDefaultValue() {
		return "";
	}

}

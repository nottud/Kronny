package datahandler.converter;

import java.util.ArrayList;
import java.util.List;

public class HalfStringConverter implements DataConverter<String> {
	
	private DataConverter<List<Character>> internalConverter;
	
	public HalfStringConverter() {
		internalConverter = new ListConverter<>(new HalfCharacterConverter());
	}

	@Override
	public String fromBytes(List<Byte> bytes, int offset) {
		List<Character> characters = internalConverter.fromBytes(bytes, offset);
		characters.remove(characters.size() - 1);
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
		characters.add((char) 0);
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

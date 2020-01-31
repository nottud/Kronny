package datahandler.converter;

import java.util.Arrays;
import java.util.List;

public class HalfCharacterConverter implements DataConverter<Character> {

	@Override
	public Character fromBytes(List<Byte> bytes, int offset) {
		return (char) (bytes.get(offset) & 0xFF);
	}

	@Override
	public List<Byte> toBytes(Character value) {
		return Arrays.asList((byte) (char) value);
	}

	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return 1;
	}

	@Override
	public Character createDefaultValue() {
		return 'A';
	}

}

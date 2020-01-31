package datahandler.converter;

import java.util.Arrays;
import java.util.List;

public class FullCharacterConverter implements DataConverter<Character> {

	@Override
	public Character fromBytes(List<Byte> bytes, int offset) {
		return (char) (bytes.get(offset) & 0xFF);
	}

	@Override
	public List<Byte> toBytes(Character value) {
		return Arrays.asList((byte) (char) value, (byte) 0x00);
	}

	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return 2;
	}
	
	@Override
	public Character createDefaultValue() {
		return 'A';
	}

}

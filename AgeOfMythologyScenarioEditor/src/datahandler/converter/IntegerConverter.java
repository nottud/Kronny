package datahandler.converter;

import java.util.Arrays;
import java.util.List;

public class IntegerConverter implements DataConverter<Integer> {

	public static final int BYTES_IN_INT = 4;

	private static final int MAX_BYTE_VALUE = 256;

	@Override
	public Integer fromBytes(List<Byte> bytes, int offset) {
		int value = Byte.toUnsignedInt(bytes.get(3 + offset));
		value = value * MAX_BYTE_VALUE + Byte.toUnsignedInt(bytes.get(2 + offset));
		value = value * MAX_BYTE_VALUE + Byte.toUnsignedInt(bytes.get(1 + offset));
		value = value * MAX_BYTE_VALUE + Byte.toUnsignedInt(bytes.get(0 + offset));
		return value;
	}

	@Override
	public List<Byte> toBytes(Integer value) {
		return Arrays.asList((byte) (int) value, (byte) (value >>> 8), (byte) (value >>> 16), (byte) (value >>> 24));
	}

	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return BYTES_IN_INT;
	}

	@Override
	public Integer createDefaultValue() {
		return 0;
	}

}

package datahandler.converter;

import java.util.List;

public class FloatConverter implements DataConverter<Float> {
	
	public static final int BYTES_IN_FLOAT = 4;
	
	private IntegerConverter integerConverter;
	
	public FloatConverter() {
		integerConverter = new IntegerConverter();
	}


	@Override
	public Float fromBytes(List<Byte> bytes, int offset) {
		return Float.intBitsToFloat(integerConverter.fromBytes(bytes, offset));
	}

	@Override
	public List<Byte> toBytes(Float value) {
		return integerConverter.toBytes(Float.floatToIntBits(value));
	}

	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return BYTES_IN_FLOAT;
	}

	@Override
	public Float createDefaultValue() {
		return 0.0F;
	}

}

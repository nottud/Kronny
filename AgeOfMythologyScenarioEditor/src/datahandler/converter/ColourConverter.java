package datahandler.converter;

import java.util.Arrays;
import java.util.List;

import javafx.scene.paint.Color;

public class ColourConverter implements DataConverter<Color> {

	public static final int BYTES_IN_COLOUR = 4;

	@Override
	public Color fromBytes(List<Byte> bytes, int offset) {
		return Color.rgb(Byte.toUnsignedInt(bytes.get(2 + offset)), Byte.toUnsignedInt(bytes.get(1 + offset)), Byte.toUnsignedInt(bytes.get(offset)));
	}

	@Override
	public List<Byte> toBytes(Color value) {
		return Arrays.asList((byte) Math.round(value.getBlue() * 255.0), (byte) Math.round(value.getGreen() * 255.0), (byte) Math.round(value.getRed() * 255.0), (byte) 0x00);
	}

	@Override
	public int getStorageLength(List<Byte> bytes, int offset) {
		return BYTES_IN_COLOUR;
	}

	@Override
	public Color createDefaultValue() {
		return Color.BLACK;
	}

}

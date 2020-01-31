package utility.bytearray;

import java.util.ArrayList;
import java.util.List;

public class ByteArrayConversion {

	public static List<Byte> box(byte[] array){
		List<Byte> list = new ArrayList<>(array.length);
		for(int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static byte[] unbox(List<Byte> list) {
		byte[] bytes = new byte[list.size()];
		for(int i = 0; i < list.size(); i++) {
			bytes[i] = list.get(i);
		}
		return bytes;
	}
	
}

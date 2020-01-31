package datahandler.converter;

import java.util.List;

public interface DataConverter<T> {
	
	public default T fromBytes(List<Byte> bytes) {
		return fromBytes(bytes, 0);
	}
	
	public T fromBytes(List<Byte> bytes, int offset);
	
	public List<Byte> toBytes(T value);
	
	public int getStorageLength(List<Byte> bytes, int offset);
	
	public T createDefaultValue();
}

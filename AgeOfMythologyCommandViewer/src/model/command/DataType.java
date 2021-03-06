package model.command;

import java.util.LinkedHashMap;
import java.util.Map;

import model.xml.Type;

public enum DataType {
	VOID("void", Type.VOID),
	BOOL("bool", Type.BOOL),
	INT("int", Type.INT),
	FLOAT("float", Type.FLOAT),
	STRING("string", Type.STRING),
	VECTOR("vector", Type.VECTOR);
	
	private static final Map<Type, DataType> TYPE_TO_DATA_TYPE = new LinkedHashMap<>();
	
	private String typeName;
	private Type type;

	
	static {
		for(DataType dataType : DataType.values()) {
		TYPE_TO_DATA_TYPE.put(null, dataType);
		}
	}
	
	private DataType(String typeName, Type type) {
		this.typeName = typeName;
		this.type = type;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return typeName;
	}
	
	public static final DataType fromType(Type type) {
		return TYPE_TO_DATA_TYPE.get(type);
	}
}

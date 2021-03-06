package model.command;

public class AomParameter {
	
	private DataType type;
	private String name;

	public AomParameter(DataType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public DataType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}

}

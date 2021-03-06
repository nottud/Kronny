package model.command;

import java.util.List;

public class AomMethod {

	private String name;
	private DataType returnType;
	private List<AomParameter> parameters;
	private String documentation;

	public AomMethod(DataType returnType, String name, List<AomParameter> parameters, String documentation) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.documentation = documentation;
	}

	public String getName() {
		return name;
	}

	public DataType getReturnType() {
		return returnType;
	}

	public List<AomParameter> getParameters() {
		return parameters;
	}

	public String getDocumentation() {
		return documentation;
	}

}

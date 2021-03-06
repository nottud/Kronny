package handler.parse.java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.command.AomMethod;
import model.command.AomParameter;
import model.command.DataType;

public class FileParser {

	private static final String JAVADOC_START = "	/**";
	private static final String JAVADOC_MID = "	 *";
	private static final String JAVADOC_END = "	 */";
	private static final String JAVADOC_END_MID = "*/";
	private static final String METHOD_START = "	public native ";

	private static final Pattern METHOD_PATTERN = Pattern.compile("(.*?) (.*?)\\((.*?)\\);");

	public Collection<AomMethod> parse(File file) throws IOException {
		Collection<AomMethod> methods = new ArrayList<>();

		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

		StringBuilder stringBuilder = new StringBuilder();
		for (String line : lines) {
			if (line.startsWith(JAVADOC_START)) {
				stringBuilder = new StringBuilder(
						stripJavaDocEnd(line.substring(JAVADOC_START.length(), line.length())));
			} else if (line.startsWith(JAVADOC_END)) {
				// Skip
			} else if (line.startsWith(JAVADOC_MID)) {
				stringBuilder.append(System.lineSeparator());
				stringBuilder.append(stripJavaDocEnd(line.substring(JAVADOC_MID.length(), line.length())));
			} else if (line.startsWith(METHOD_START)) {
				parseMethod(line.substring(METHOD_START.length(), line.length()), stringBuilder.toString())
						.ifPresent(methods::add);
				stringBuilder = new StringBuilder();
			}
		}
		return methods;
	}

	private String stripJavaDocEnd(String javadoc) {
		if (javadoc.endsWith(JAVADOC_END_MID)) {
			return javadoc.substring(0, javadoc.length() - JAVADOC_END_MID.length()).trim();
		} else {
			return javadoc.trim();
		}
	}

	private Optional<AomMethod> parseMethod(String line, String javadoc) {
		Matcher matcher = METHOD_PATTERN.matcher(line);
		if (matcher.matches()) {
			String returnType = matcher.group(1);
			String methodName = matcher.group(2);
			String methodParameters = matcher.group(3);

			Optional<DataType> parsedReturnType = parseDataType(returnType);
			if (parsedReturnType.isPresent()) {
				return Optional.of(new AomMethod(parsedReturnType.get(), methodName,
						parseParameterList(methodParameters), javadoc));
			}
		}
		return Optional.empty();
	}

	private Optional<DataType> parseDataType(String typeString) {
		for (DataType dataType : DataType.values()) {
			if (dataType.getTypeName().equals(typeString)) {
				return Optional.of(dataType);
			}
		}
		return Optional.empty();
	}

	private List<AomParameter> parseParameterList(String parameterAsString) {
		List<AomParameter> parameters = new ArrayList<>();
		for (String parameterString : parameterAsString.split(",")) {
			parseParameter(parameterString.trim()).ifPresent(parameters::add);
		}
		return parameters;
	}

	private Optional<AomParameter> parseParameter(String parameter) {
		String[] split = parameter.split(" ");
		if (split.length != 2) {
			return Optional.empty();
		} else {
			Optional<DataType> parsedDataType = parseDataType(split[0]);
			if (!parsedDataType.isPresent()) {
				return Optional.empty();
			} else {
				return Optional.of(new AomParameter(parsedDataType.get(), split[1]));
			}
		}
	}

}

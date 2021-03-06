package handler.io.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import model.command.AomMethod;
import model.command.AomParameter;
import model.command.CommandModel;
import model.command.DataType;
import model.xml.Model;
import model.xml.Model.Method;
import model.xml.Parameter;
import model.xml.SchemaRoot;

public class LoadFromXml {

	public void load(CommandModel commandModel, File chosenFile) throws JAXBException, SAXException {
		JAXBContext context = JAXBContext.newInstance(Model.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		unmarshaller.setSchema(schemaFactory.newSchema(SchemaRoot.class.getResource("schema.xsd")));
		Object object = unmarshaller.unmarshal(chosenFile);
		
		Model model = (Model) object;
		for(Method xmlMethod : model.getMethod()) {
			DataType returnType = DataType.fromType(xmlMethod.getReturn());
			String name = xmlMethod.getName();
			List<AomParameter> parameters = new ArrayList<>();
			for (Parameter xmlParameter : xmlMethod.getParameters()) {
				parameters.add(new AomParameter(DataType.fromType(xmlParameter.getType()), xmlMethod.getName()));
			}
			String javadoc = xmlMethod.getJavadoc();
			commandModel.add(new AomMethod(returnType, name, parameters, javadoc));
		}
	}

}

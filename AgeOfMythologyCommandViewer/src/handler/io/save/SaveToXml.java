package handler.io.save;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import model.command.AomMethod;
import model.command.AomParameter;
import model.command.CommandModel;
import model.xml.Model;
import model.xml.Model.Method;
import model.xml.Parameter;
import model.xml.SchemaRoot;

public class SaveToXml {

	public void save(CommandModel commandModel, File chosenFile) throws JAXBException, SAXException {
		Model model = new Model();
		for (AomMethod method : commandModel.getMethods()) {
			Method xmlMethod = new Method();
			xmlMethod.setReturn(method.getReturnType().getType());
			xmlMethod.setName(method.getName());
			for (AomParameter parameter : method.getParameters()) {
				Parameter xmlParameter = new Parameter();
				xmlParameter.setName(parameter.getName());
				xmlParameter.setType(parameter.getType().getType());
				xmlMethod.getParameters().add(xmlParameter);
			}
			xmlMethod.setJavadoc(method.getDocumentation());
			model.getMethod().add(xmlMethod);
		}

		JAXBContext context = JAXBContext.newInstance(Model.class);
		Marshaller marshaller = context.createMarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		marshaller.setSchema(schemaFactory.newSchema(SchemaRoot.class.getResource("schema.xsd")));
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(model, chosenFile);
	}

}

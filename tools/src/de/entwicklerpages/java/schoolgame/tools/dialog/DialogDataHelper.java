package de.entwicklerpages.java.schoolgame.tools.dialog;

import org.xml.sax.SAXException;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import de.entwicklerpages.java.schoolgame.game.dialog.Level;
import de.entwicklerpages.java.schoolgame.tools.PathHelper;

public final class DialogDataHelper {
    private DialogDataHelper() {}

    private static JAXBContext jaxbContext = null;

    private static JAXBContext getJaxbContext() throws JAXBException {
        if (jaxbContext == null)
        {
            jaxbContext = JAXBContext.newInstance(Level.class);
        }
        return jaxbContext;
    }

    public static Level getDialogRoot(File dialogFile) throws JAXBException, SAXException
    {
        Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
        unmarshaller.setSchema(getSchema());

        return (Level) unmarshaller.unmarshal(dialogFile);
    }

    public static void saveDialogRoot(File dialogFile, Level root) throws JAXBException, SAXException
    {

        Marshaller marshaller = getJaxbContext().createMarshaller();
        marshaller.setSchema(getSchema());
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(root, dialogFile);
    }

    private static Schema getSchema() throws SAXException
    {
        File schemaFile = new File(PathHelper.getDialogDirIfFound().getAbsolutePath() + File.separator + "dialog.xsd");

        if (!schemaFile.exists() || !schemaFile.canRead())
            return null;

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        return factory.newSchema(schemaFile);
    }
}

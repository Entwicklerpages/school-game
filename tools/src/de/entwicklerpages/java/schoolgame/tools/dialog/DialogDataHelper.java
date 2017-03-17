package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.entwicklerpages.java.schoolgame.game.dialog.Level;

// TODO Load dialog schema file
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

    public static Level getDialogRoot(File dialogFile) throws JAXBException
    {
        Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
        //unmarshaller.setSchema(dialogSchema);

        return (Level) unmarshaller.unmarshal(dialogFile);
    }

    public static void saveDialogRoot(File dialogFile, Level root) throws JAXBException {

        Marshaller marshaller = getJaxbContext().createMarshaller();
        //marshaller.setSchema(dialogSchema);

        marshaller.marshal(root, dialogFile);
    }
}

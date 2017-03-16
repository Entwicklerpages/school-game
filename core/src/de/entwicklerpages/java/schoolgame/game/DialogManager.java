package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.Level;

/**
 * Lädt eine Dialogdatei und zeigt Dialoge bei Bedarf an.
 *
 * @author nico
 */
public class DialogManager {

    private final static String SCHEMA_XSD = "dialog/dialog.xsd";

    public DialogManager(String dialogFileName) throws Exception {
        Gdx.app.log("INFO", "Start loading dialog file " + dialogFileName);

        loadDialogFile(dialogFileName);

        Gdx.app.log("INFO", "Dialog file loaded.");
    }

    private void loadDialogFile(String fileName) throws Exception {
        FileHandle dialogFile = Gdx.files.internal("dialog/" + fileName + ".xml");

        if (!dialogFile.exists() || dialogFile.isDirectory())
        {
            Gdx.app.error("ERROR", "The dialog file " + fileName + ".xml doesn't exists!");
            throw new FileNotFoundException("The dialog file " + fileName + ".xml doesn't exists!");
        }

        Schema dialogSchema;

        try
        {
            dialogSchema = getSchema(dialogFile);
        } catch (Exception e)
        {
            Gdx.app.error("ERROR", "The dialog file " + fileName + ".xml is not conform to the dialog.xsd!", e);
            throw new Exception("The dialog file " + fileName + ".xml is not conform to the dialog.xsd!");
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(Level.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(dialogSchema);

        Level level = (Level) unmarshaller.unmarshal(dialogFile.read());

        Gdx.app.log("TEST", "ATLAS: " + level.getAtlas());

        for (DialogType dialog : level.getDialogs().getDialog())
        {
            Gdx.app.log("TEST", "Found dialog: " + dialog.getName());
        }

    }

    private Schema getSchema(FileHandle dialogFile) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(Gdx.files.internal(SCHEMA_XSD).read()));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(dialogFile.read()));

        return schema;
    }
}

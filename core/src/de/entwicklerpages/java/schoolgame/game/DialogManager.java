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

import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.Level;
import de.entwicklerpages.java.schoolgame.game.dialog.StatementType;

/**
 * Lädt eine Dialogdatei und zeigt Dialoge bei Bedarf an.
 *
 * @author nico
 */
public class DialogManager {

    private final static String SCHEMA_XSD = "dialog/dialog.xsd";

    /**
     * Zugriff auf die Spielinstanz
     *
     * @see SchoolGame
     */
    protected SchoolGame game;

    private Level dialogRoot;

    public DialogManager(SchoolGame game, String dialogFileName) throws Exception {
        Gdx.app.log("INFO", "Start loading dialog file " + dialogFileName);

        this.game = game;

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

        dialogRoot = (Level) unmarshaller.unmarshal(dialogFile.read());

        for (DialogType dialog : dialogRoot.getDialogs().getDialog())
        {
            for (StatementType statement : dialog.getStatement())
            {
                Gdx.app.log("DIALOG", "Statement Start");
                for (String text : statement.getTexts().getText())
                {
                    Gdx.app.log("DIALOG", "Text: " + text);
                }
            }
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

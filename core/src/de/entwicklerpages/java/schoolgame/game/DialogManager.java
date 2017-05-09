package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import de.entwicklerpages.java.schoolgame.AudioManager;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.common.InputManager;
import de.entwicklerpages.java.schoolgame.game.dialog.*;
import de.entwicklerpages.java.schoolgame.game.dialog.Level;

/**
 * Lädt eine Dialogdatei und zeigt Dialoge bei Bedarf an.
 *
 * @author nico
 */
public class DialogManager {

    private final static Color ROYAL_DARK = new Color(0x2251ddff);

    private final static String SCHEMA_XSD = "data/dialog/dialog.xsd";
    private static final float HEIGHT = 220f;

    /**
     * Zugriff auf die Spielinstanz
     *
     * @see SchoolGame
     */
    protected final SchoolGame game;

    private ActionCallback finishedCallback = null;

    private final String playerName;

    private final BitmapFont textFont;
    private final GlyphLayout fontLayout;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Matrix4 projection;

    private Level dialogRoot;

    private Map<String, CharacterType> characterMap;

    private DialogType activeDialog = null;
    private String talking = null;
    private String[] lines = null;
    private int activeStatement = 0;
    private int activeText = 0;
    private AudioManager.SoundKey statementSound = null;

    public DialogManager(SchoolGame game, String dialogFileName, String playerName) throws Exception {
        Gdx.app.log("INFO", "Start loading dialog file " + dialogFileName);

        this.game = game;
        this.playerName = playerName;

        textFont = game.getLongTextFont();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        fontLayout = new GlyphLayout();

        projection = new Matrix4();

        loadDialogFile(dialogFileName);

        Gdx.app.log("INFO", "Dialog file loaded.");
    }

    private void loadDialogFile(String fileName) throws Exception {
        FileHandle dialogFile = Gdx.files.internal("data/dialog/" + fileName + ".xml");

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

        loadCharacters();
    }

    private void loadCharacters()
    {
        characterMap = new HashMap<String, CharacterType>(dialogRoot.getCharacters().getCharacter().size());

        for (CharacterType character : dialogRoot.getCharacters().getCharacter())
        {
            characterMap.put(character.getId(), character);
        }
    }

    private Schema getSchema(FileHandle dialogFile) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(Gdx.files.internal(SCHEMA_XSD).read()));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(dialogFile.read()));

        return schema;
    }

    public void startDialog(String dialogName)
    {
        for (DialogType dialogType : dialogRoot.getDialogs().getDialog())
        {
            if (dialogType.getName().equals(dialogName))
            {
                activeDialog = dialogType;
                activeText = 0;      // Diese Zeilen sind eigentlich redundant, allerdings stellen wir damit sicher,
                activeStatement = 0; // Das ein neuer Dialog wirklich von vorne startet.
                prepareText();
                return;
            }
        }

        Gdx.app.error("WARNING", "Dialog '" + dialogName + "' was not found!");

        if (finishedCallback != null)
            finishedCallback.run();

        finishedCallback = null;
    }

    private void nextLine()
    {
        if (statementSound != null)
        {
            game.getAudioManager().unloadSound(statementSound);
            statementSound = null;
        }

        activeText++;
        int textSize = activeDialog.getStatement().get(activeStatement).getTexts().getText().size();

        if (activeText >= textSize)
        {
            activeText = 0;
            activeStatement++;
            int statementSize = activeDialog.getStatement().size();

            if (activeStatement >= statementSize)
            {
                activeStatement = 0;
                activeDialog = null;

                if (finishedCallback != null)
                    finishedCallback.run();

                finishedCallback = null;

                return;
            }
        }

        prepareText();
    }

    private void prepareText()
    {
        if (activeText == 0)
        {
            String soundName = activeDialog.getStatement().get(activeStatement).getSound();
            if (soundName != null && !soundName.isEmpty())
            {
                statementSound = game.getAudioManager().createSound("dialog", soundName);
                game.getAudioManager().playSound(statementSound);
            }
        }

        talking = activeDialog.getStatement().get(activeStatement).getTalking();

        CharacterType character = characterMap.get(talking);
        if (character != null)
            talking = character.getTitle();
        else
            Gdx.app.error("WARNING", "There is no character '" + talking + "' who can talk in '" + activeDialog.getName() + "', statement: " + activeStatement);

        String nextText = activeDialog.getStatement().get(activeStatement).getTexts().getText().get(activeText);
        lines = nextText.replaceAll("(#\\{PLAYER\\})", playerName).split("(\\|br\\|)+");

        if (lines.length > 3)
        {
            Gdx.app.error("WARNING", "Too many dialog lines in '" + activeDialog.getName() + "', statement: " + activeStatement + " text: " + activeText + ". Line count: " + lines.length);
        }
    }

    public void render(OrthographicCamera camera, float deltaTime)
    {
        if (!isPlaying()) return;

        // Kameraprojektion anpassen
        projection.set(camera.projection).translate(-camera.viewportWidth / 2f, -camera.viewportHeight / 2f, 0f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        shapeRenderer.setProjectionMatrix(projection);

        /////////////////

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 0.4f);
        shapeRenderer.rect(0f, 0f, camera.viewportWidth, camera.viewportHeight);

        shapeRenderer.setColor(1.0f, 0.93725f, 0.77647f, 1.0f);
        shapeRenderer.rect(0f, 0f, camera.viewportWidth, HEIGHT);

        shapeRenderer.end();

        /////////////////

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.line(0f, HEIGHT + 1, camera.viewportWidth, HEIGHT + 1);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(0f, HEIGHT, camera.viewportWidth, HEIGHT);

        shapeRenderer.end();

        //////////////////

        Gdx.gl.glDisable(GL20.GL_BLEND);

        //////////////////

        batch.setProjectionMatrix(projection);
        batch.begin();

        float y = camera.viewportHeight - (camera.viewportHeight - HEIGHT);

        y -= 30f;

        fontLayout.setText(textFont, talking, ROYAL_DARK, camera.viewportWidth - 60, Align.left, false);
        textFont.draw(batch, fontLayout, 30, y);

        y -= 50;

        for (String line : lines)
        {
            fontLayout.setText(textFont, line, Color.BLACK, camera.viewportWidth - 60, Align.left, false);
            textFont.draw(batch, fontLayout, 30, y);

            y -= 35f;
        }

        batch.end();
    }

    public boolean handleInput(int keycode)
    {
        if (!isPlaying()) return false;

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE || keycode == Input.Keys.TAB)
        {
            nextLine();
            return true;
        }

        return false;
    }

    public boolean isPlaying()
    {
        return activeDialog != null;
    }


    public void setFinishedCallback(ActionCallback finishedCallback)
    {
        this.finishedCallback = finishedCallback;
    }
}

package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SchoolGame implements ApplicationListener {
	private SpriteBatch batch;
	private Texture img;

	/***
	 * Wird zum Start einmal aufgerufen.
	 * Hier werden alle benötigten Variablen initialisiert.
	 */
	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	/***
	 * Wird aufgerufen, wenn sich die Fenstergröße ändert.
	 *
	 * @param width	Die neue Breite des Fensters
	 * @param height Die neue Höhe des Fensters
     */
	@Override
	public void resize(int width, int height) {

	}

	/***
	 * Hauptschleife des Spiels.
	 */
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	/***
	 * Wird kurz vor dem Beenden aufgerufen.
	 * Perfekter Ort, um die Spieldaten zu speichern, wenn nötig.
 	 */
    @Override
	public void pause() {

	}

	/**
	 * Wird auf der Desktop-Platform nie aufgerufen.
	 */
	@Override
	public void resume() {
	}

	/***
	 * Hier werden alle Resourcen freigegeben.
	 */
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}

package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SchoolGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Viewport viewport;

	/***
	 * Wird zum Start einmal aufgerufen.
	 * Hier werden alle benötigten Variablen initialisiert.
	 */
	@Override
	public void create() {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();

		viewport = new FitViewport(1280, 720, camera);
		viewport.apply();
	}

	/***
	 * Wird aufgerufen, wenn sich die Fenstergröße ändert.
	 *
	 * @param width	Die neue Breite des Fensters
	 * @param height Die neue Höhe des Fensters
     */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	}

	/***
	 * Hauptschleife des Spiels.
	 */
	@Override
	public void render() {

		camera.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.end();
	}

	/***
	 * Wird kurz vor dem Beenden aufgerufen.
	 * Brauchen wir nicht.
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
	}
}

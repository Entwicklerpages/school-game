package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import static com.badlogic.gdx.graphics.g2d.Batch.C1;
import static com.badlogic.gdx.graphics.g2d.Batch.C2;
import static com.badlogic.gdx.graphics.g2d.Batch.C3;
import static com.badlogic.gdx.graphics.g2d.Batch.C4;
import static com.badlogic.gdx.graphics.g2d.Batch.U1;
import static com.badlogic.gdx.graphics.g2d.Batch.U2;
import static com.badlogic.gdx.graphics.g2d.Batch.U3;
import static com.badlogic.gdx.graphics.g2d.Batch.U4;
import static com.badlogic.gdx.graphics.g2d.Batch.V1;
import static com.badlogic.gdx.graphics.g2d.Batch.V2;
import static com.badlogic.gdx.graphics.g2d.Batch.V3;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;
import static com.badlogic.gdx.graphics.g2d.Batch.X1;
import static com.badlogic.gdx.graphics.g2d.Batch.X2;
import static com.badlogic.gdx.graphics.g2d.Batch.X3;
import static com.badlogic.gdx.graphics.g2d.Batch.X4;
import static com.badlogic.gdx.graphics.g2d.Batch.Y1;
import static com.badlogic.gdx.graphics.g2d.Batch.Y2;
import static com.badlogic.gdx.graphics.g2d.Batch.Y3;
import static com.badlogic.gdx.graphics.g2d.Batch.Y4;


/**
 * Verbesserter Renderer. Dieser rendert auch gleich Objekte wieden Spieler mit.
 * Dazu müssen diese sich als ExtendedMapDisplayObject registrieren.
 *
 * @see ExtendedMapDisplayObject
 * @see ExtendedOrthogonalTiledMapRenderer#addDisplayObject(ExtendedMapDisplayObject)
 *
 * @author nico
 */
public class ExtendedOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Der Layer, in dem auch die Objekte angezeigt werden.
     */
    private TiledMapTileLayer displayLayer;

    /**
     * Eine Liste aller Objekte die gerendert werden sollen.
     */
    private Array<ExtendedMapDisplayObject> displayObjects;

    /**
     * Eine Liste aller Objekte, die innerhalb eines Frames noch nicht gerendert wurden.
     */
    private Array<ExtendedMapDisplayObject> renderObjects;

    /**
     * Instanz eines Komperators. Dieser sortiert die Objekte nach deren Tiefe.
     */
    private DepthComparator depthComparator;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inititalisierung.
     *
     * @param map die Map, die gerendert werden soll
     * @param displayLayer der Layer in der Map, in dem auch die Objekte gerendert werden
     */
    public ExtendedOrthogonalTiledMapRenderer (TiledMap map, TiledMapTileLayer displayLayer) {
        super(map);
        this.displayLayer = displayLayer;
        this.displayObjects = new Array<ExtendedMapDisplayObject>(false, 20);
        this.renderObjects = new Array<ExtendedMapDisplayObject>(true, 20);
        this.depthComparator = new DepthComparator();
    }

    /**
     * Rendert alle Layer und alle Objekte
     *
     * @param deltaTime die Zeit, die seit dem letztem Frame vergangen ist
     */
    public void renderAll(float deltaTime) {
        beginRender();
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {

                    if (layer == displayLayer)
                        renderDisplayLayer((TiledMapTileLayer) layer, deltaTime);
                    else
                        renderTileLayer((TiledMapTileLayer)layer);
                } else if (layer instanceof TiledMapImageLayer) {
                    renderImageLayer((TiledMapImageLayer)layer);
                } else {
                    renderObjects(layer);
                }
            }
        }
        endRender();
    }

    /**
     * Fügt ein Objekt zum Rendern hinzu.
     *
     * @param newObject das neue Objekt
     */
    public void addDisplayObject(ExtendedMapDisplayObject newObject)
    {
        displayObjects.add(newObject);
    }

    /**
     * Entfernt ein Objekt.
     *
     * ACHTUNG: Es wird ein == vergleich und keine equals durchgeführt.
     * Beim Entfernen MUSS es sich um die selbe Instanz handeln.
     *
     * @param removeObject das zu entfernende Objekt
     */
    public void removeDisplayObject(ExtendedMapDisplayObject removeObject)
    {
        displayObjects.removeValue(removeObject, true);
    }

    /**
     * Zeigt die Ebene an, in der auch die Objekte gerendert werden.
     * Der Code wurde vom OrthogonalTiledMapRenderer übernommen und in der
     * Mitte angepasst um Objekte an der richtigen Stelle zu rendern.
     *
     * @param layer der Layer, der gerendert werden soll
     * @param deltaTime die Zeit, die seit dem letztem Frame vergangen ist
     */
    private void renderDisplayLayer(TiledMapTileLayer layer, float deltaTime)
    {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * unitScale;
        final float layerTileHeight = layer.getTileHeight() * unitScale;

        final int col1 = Math.max(0, (int)(viewBounds.x / layerTileWidth));
        final int col2 = Math.min(layerWidth, (int)((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

        final int row1 = Math.max(0, (int)(viewBounds.y / layerTileHeight));
        final int row2 = Math.min(layerHeight, (int)((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;
        final float[] vertices = this.vertices;

        // TODO Benchmark
        //displayObjects.sort(depthComparator); // Möglicherweise sollte lieber die Hauptliste sortiert werden. Da die Position der Objekte oft gleich bleibt müssen seltener Objekte verschoben werden.

        renderObjects.addAll(displayObjects); // Nicht unbedingt die beste Methode aber es funktioniert.
        renderObjects.sort(depthComparator);

        for (int row = row2; row >= row1; row--) {

            // Alle Objekte mit größerem Y Wert zeichnen

            for (ExtendedMapDisplayObject displayObject : renderObjects)
            {
                if (displayObject.getPosY() > y)
                {
                    displayObject.render(batch, deltaTime);
                    renderObjects.removeValue(displayObject, true);
                } else {
                    break; // Die Liste ist sortiert, also sollte hier nichts sinnvolles mehr kommen.
                }
            }

            // Kacheln zeichnen

            float x = xStart;
            for (int col = col1; col < col2; col++) {
                final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) {
                    x += layerTileWidth;
                    continue;
                }
                final TiledMapTile tile = cell.getTile();

                if (tile != null) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() * unitScale;
                    float y1 = y + tile.getOffsetY() * unitScale;
                    float x2 = x1 + region.getRegionWidth() * unitScale;
                    float y2 = y1 + region.getRegionHeight() * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
                }
                x += layerTileWidth;
            }
            y -= layerTileHeight;
        }

        renderObjects.clear();
    }

    /**
     * Vergleicht ExtendedMapDisplayObjects nach deren Y Position.
     * Diese Position wird mit der Tiefe der Objekte gleichgesetzt.
     *
     * @see ExtendedMapDisplayObject#getPosY()
     *
     * @author nico
     */
    private class DepthComparator implements Comparator<ExtendedMapDisplayObject>
    {
        @Override
        public int compare(ExtendedMapDisplayObject object1, ExtendedMapDisplayObject object2) {


            if(object1.getPosY() > object2.getPosY())
                return -1;
            else if (object1.getPosY() < object2.getPosY())
                return 1;

            return 0;
        }
    }
}

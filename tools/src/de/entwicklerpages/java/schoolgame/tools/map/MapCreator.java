package de.entwicklerpages.java.schoolgame.tools.map;


@SuppressWarnings("FieldCanBeLocal")
public final class MapCreator
{
    private MapCreator() {}

    public static String createMap()
    {
        String mapCode = HEAD;

        mapCode += LAYER.replaceAll(REGEX_LAYER, "Hintergrund");
        mapCode += LAYER.replaceAll(REGEX_LAYER, "Dekoration");
        mapCode += OBJECTS;
        mapCode += LAYER.replaceAll(REGEX_LAYER, "Vordergrund");
        mapCode += FOOTER;

        return mapCode;
    }

    private static final String REGEX_LAYER = "\\{\\{LAYER_NAME\\}\\}";

    private static final String HEAD =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"right-down\" width=\"50\" height=\"50\" tilewidth=\"32\" tileheight=\"32\" nextobjectid=\"2\">\n";

    private static final String LAYER =
                    " <layer name=\"{{LAYER_NAME}}\" width=\"50\" height=\"50\">\n" +
                    "  <data encoding=\"base64\" compression=\"zlib\">\n" +
                    "   eJztwQENAAAAwqD3T20ON6AAAAAAAAAAAADg3wAnEAAB\n" +
                    "  </data>\n" +
                    " </layer>\n";

    private static final String OBJECTS =
                    " <objectgroup name=\"Objekte\">\n" +
                    "  <object id=\"1\" name=\"Spielerstart\" type=\"Start\" x=\"10\" y=\"10\" width=\"10\" height=\"10\">\n" +
                    "   <ellipse/>\n" +
                    "  </object>\n" +
                    " </objectgroup>\n" +
                    " <objectgroup name=\"Kollisionen\"/>\n";

    private static final String FOOTER =
                    "</map>\n";
}

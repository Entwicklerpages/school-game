In dieses Verzeichnis kommen alle fertigen Dialoge im XML Format.
Jedes Level hat seine eigene Datei.

Sollte das Schema dialog.xsd geändert werden, müssen die JAXB Klassen neu generiert werden.
Dazu sollte die dialog.xsd in ein anderes Verzeichnis kopiert werden und dann in dem Verzeichnis
folgender Befehl ausgeführt werden:

xjc -no-header -p de.entwicklerpages.java.schoolgame.game.dialog -encoding UTF-8 dialog.xsd

<?xml version="1.0" encoding="UTF-8"?>
<tileset name="cave" tilewidth="32" tileheight="32" tilecount="48" columns="8">
 <image source="cave_map.png" width="256" height="192"/>
 <terraintypes>
  <terrain name="Wasser" tile="39"/>
  <terrain name="Boden" tile="25"/>
  <terrain name="Wand" tile="20"/>
 </terraintypes>
 <tile id="2" terrain="1,1,1,0">
  <animation>
   <frame tileid="0" duration="300"/>
   <frame tileid="2" duration="300"/>
  </animation>
 </tile>
 <tile id="3" terrain="1,1,0,1">
  <animation>
   <frame tileid="3" duration="300"/>
   <frame tileid="1" duration="300"/>
  </animation>
 </tile>
 <tile id="4" terrain="1,0,1,0">
  <animation>
   <frame tileid="4" duration="300"/>
   <frame tileid="12" duration="300"/>
  </animation>
 </tile>
 <tile id="5" terrain="0,1,0,1">
  <animation>
   <frame tileid="5" duration="300"/>
   <frame tileid="13" duration="300"/>
  </animation>
 </tile>
 <tile id="10" terrain="1,0,1,1">
  <animation>
   <frame tileid="10" duration="300"/>
   <frame tileid="8" duration="300"/>
  </animation>
 </tile>
 <tile id="11" terrain="0,1,1,1">
  <animation>
   <frame tileid="9" duration="300"/>
   <frame tileid="11" duration="300"/>
  </animation>
 </tile>
 <tile id="16" terrain="2,2,2,1"/>
 <tile id="17" terrain="2,2,1,1"/>
 <tile id="18" terrain="2,2,1,2"/>
 <tile id="19" terrain="2,2,2,2"/>
 <tile id="20" terrain="2,2,2,2"/>
 <tile id="21" terrain="2,2,2,2"/>
 <tile id="22" terrain="0,0,0,1">
  <animation>
   <frame tileid="6" duration="300"/>
   <frame tileid="22" duration="300"/>
  </animation>
 </tile>
 <tile id="23" terrain="0,0,1,0">
  <animation>
   <frame tileid="7" duration="300"/>
   <frame tileid="23" duration="300"/>
  </animation>
 </tile>
 <tile id="24" terrain="2,1,2,1"/>
 <tile id="25" terrain="1,1,1,1"/>
 <tile id="26" terrain="1,2,1,2"/>
 <tile id="29" terrain="2,2,,"/>
 <tile id="30" terrain="0,1,0,0">
  <animation>
   <frame tileid="14" duration="300"/>
   <frame tileid="30" duration="300"/>
  </animation>
 </tile>
 <tile id="31" terrain="1,0,0,0">
  <animation>
   <frame tileid="15" duration="300"/>
   <frame tileid="31" duration="300"/>
  </animation>
 </tile>
 <tile id="32" terrain="2,1,2,2"/>
 <tile id="33" terrain="1,1,2,2"/>
 <tile id="34" terrain="1,2,2,2"/>
 <tile id="35" terrain="2,,2,"/>
 <tile id="36" terrain=",2,,2"/>
 <tile id="37" terrain=",,2,2"/>
 <tile id="39" terrain="0,0,0,0">
  <animation>
   <frame tileid="38" duration="300"/>
   <frame tileid="39" duration="300"/>
  </animation>
 </tile>
 <tile id="42" terrain="1,1,1,1"/>
 <tile id="45" terrain="1,1,0,0">
  <animation>
   <frame tileid="44" duration="300"/>
   <frame tileid="45" duration="300"/>
  </animation>
 </tile>
 <tile id="47" terrain="0,0,1,1">
  <animation>
   <frame tileid="47" duration="300"/>
   <frame tileid="46" duration="300"/>
  </animation>
 </tile>
</tileset>

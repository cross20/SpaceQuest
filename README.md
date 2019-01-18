# SpaceQuest

Final project for CS372 Java Application Development.<br>
Authors: Kyle Shepard, Chad Ross.<br>

<p>SpaceQuest is a game that will take a player on an adventure through a space ship. On this adventure the player will fight enemies while trying to reach the end of the ship. As the game progresses, enemy entities will become stronger making the game more difficult and interesting.<br>

Some of the main features of the game will include health packs and weapon drops. These will allow the player to build up their strength as they face more difficult enemies. Also, the game will be room-based. This means that the game will load rooms as the player enter them. In other words, the map is not continuous.<br></p>

<h6>Game features</h6>
<ul>
<li>Single player.</li>
<li>Enemy entities.</li>
<li>Entity health.</li>
<li>Entity momentum</li>
<li>Combat...</li>
<ul>
<li>Hand to hand.</li>
<li>Trajectory based weapons.</li>
<li>Knock back.</li>
</ul>
<li>Head-up Display (HUD).</li>
<li>Gampepad support.</li>
</ul>

<p>As part of the implementation of these features, SpaceQuest will need the following hierarchy. A parent class "Entity" will define shared methods between players and enemies. The child classes "Player" and "Enemy" will define their type specific methods. The class "SpaceQuest" will contain the main game mechanics. Additionally, the library <a href="https://github.com/williamahartman/Jamepad">Jamepad</a> will be used to allow for controllers to be used in the game. More classes may be needed in the future.<br></p>

![alt text](https://github.com/cross20/SpaceQuest/blob/master/documents/UML.png)

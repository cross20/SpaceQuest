# SpaceQuest

Final project for CS372 Java Application Development.<br>
Authors: Kyle Shepard, Chad Ross.<br>

<p>SpaceQuest is a game that takes a player on an adventure through a space ship. On this adventure the player can explore many rooms while making their way from one end of the ship to the other. These rooms all connect and can be accessed by leaving the map.<br>

While not yet implemented, the game will soon contain enemies for the player to fight. These enemies will prevent the player reaching the end of the ship. To fight back, the player will soon have the ability to shoot the enemies, eventually reaching the end of the ship.<br></p>

<h6>Game features</h6>
<ul>
<li>Single player.</li>
<li>Entity health.</li>
<li>Gampepad support.</li>
</ul>

<p>As part of the implementation of these features, SpaceQuest has the following class hierarchy. A parent class "Entity" defines shared methods between players and enemies. The child classes "Player" and "Enemy" define their type specific methods. The class "SpaceQuest" contains the main game mechanics and runs multiple threads to track all of the game updates. Additionally, the class "Room" defines the boundaries of each room in the map and runs checks against these boundaries to make sure entity objects stay within these bounds. Other classes that were needed were RotateLabel (which extends JLabel), MainMenu, and Projectile (which extends Entity). These were all combined with the library <a href="https://github.com/williamahartman/Jamepad">Jamepad</a> which is used to allow for controllers to be used in the game.<br></p>

![alt text](https://github.com/cross20/SpaceQuest/blob/master/documents/UML.png)

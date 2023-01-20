# Othello
Reversi / Othello - DTU Software January Project 2023

## How to run

```shell
 .\jdk-19-fx\bin\java.exe -jar --enable-preview --enable-native-access=ALL-UNNAMED .\build\libs\Othello.jar
```

Alternatively it can be run with any Java 19 installation that includes JavaFX without arguments as such:

```shell
java -jar Othello.jar
```

This works as App.java starts a fork of itself with the required arguments if it detects that they are missing.

You can also use Gradle to run the project:

```shell
gradle run
```

## How to build

If you want to build the project yourself, you can do so by running the following command:

```shell
gradle build 
```

It outputs the jar file to `build/libs/Othello.jar`

## How to play

Simply start the jar file with any of the above methods.

In the initial "Home" menu you can select "Play Game" to be taken to the main game scene which contains most functionality for interactivity.

Optionally between games you can navigate to the "Settings" menu to change the board size, the number of players, and more.

It should be noted these changes only take effect when a new game is started either via. the "New Game" option in the "Pause Menu" which is easily accessible from the game screen by pressing "Esc" or clicking the "Menu" button in the top left hand corner of the scene.

Once you are playing a game you simply interact with either the 2D or 3D version of the game which you can toggle using the "3D Mode" button from either the top-bar or the "Pause" menu.

Unless the Setup box has been unchecked in the settings menu, the first phase consists of all players placing their start pieces based on the max placement setting, otherwise a default player count x player count grid is formed.

You can navigate a "timeline" of your previous moves by using the < and > buttons that appear to the side when moves are available.

And by double-clicking, or clicking a selected preview Move state you can revert the entire game back to that point, losing the moves that were made after that point.

You can save the current game at any point by pressing the save button and inputting the desired name of the save, afterwhich you can confirm the save by clicking "Enter" or pressing the button again.

Saving a game under the same name will overwrite the previous save.

You can load a previously saved game by pressing the corresponding button on the "Home" menu.

## Known issues and limitations

Due to the physical constraints of creating 3D models, the 3D mode only supports up to a 8 by 8 board with up to 2 players.

If you wish to revert back to the initial starting positions of the game, simply reverting using the timeline is not enough, and you need to Save and reload the game afterwards.


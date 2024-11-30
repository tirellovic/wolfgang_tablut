# Tablut Competition 2024/2025
Software for the Tablut Students Competition.

## Our strategy

This project implements an artificial player for the game Tablut, designed to make intelligent, efficient decisions while playing as either side (Black or White). The AI is based on the Iterative Deepening Search algorithm combined with Alpha-Beta Pruning (exposed by the AIMA library) to reduce the search space and improve performance.

**Black Player**

Evaluates the state by prioritizing the survival of black pawns, the capture of white pawns, and the king’s proximity to black pawns, aiming to restrict the king’s mobility and ensure a quick victory.

**White Player**

Considers the strategic positioning of white pawns, the number of captured black pawns, protection for the king, and escape routes to maximize the chances of victory for the white side.

**Action Ordering**

A new custom class extends the base search class (*IterativeDeepeningAlphaBetaSearch*) to implement action ordering based on the evaluation of moves using the defined heuristics. This ensures that the most promising actions are explored earlier in the Alpha-Beta pruning process, improving both computational efficiency and strategic depth.

## Requirements

From console, run these commands to install JDK 8:

```
sudo apt update
sudo apt install openjdk-8-jdk -y
```

Now, clone the project repository:

```
git clone https://github.com/tirellovic/wolfgang_tablut.git
```

## Run the Server

To start the game, move in `jars` directory and run the Server with this command:
```
java -jar Server.jar
```

To run the game as either black or white artificial player:

```
./runmyplayer WHITE 60 localhost
```
```
./runmyplayer BLACK 60 localhost
```

In alternative, you can also compile and run the project with `ant`:
```
sudo apt install ant -y
```

After installing `ant`,compile the project:
```
ant clean
ant compile
```
The compiled project is in the build folder. Run the server with:
```
ant server
```
If you want to see the game through the gui, run this command:
```
ant gui-server
```
Check the behaviour using the artificial players in two different console windows:
```
ant artificialwhite
ant artificialblack
```
At this point, a window with the game state should appear.

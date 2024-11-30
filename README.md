# Tablut Competition 2024/2025
Software for the Tablut Students Competition
## Our strategy

BLABLABLA

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
./runmyplayer white 60 localhost
```
```
./runmyplayer black 60 localhost
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

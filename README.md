# Tablut Competition 2024/2025
Software for the Tablut Students Competition

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

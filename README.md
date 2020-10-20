# Group 19 Game

[Trailer:](https://youtu.be/-cjyODFS3DQ)


## Build

Within the root directory run:

```bash
mvn package -Pdesktop
```

This will build the game at `desktop/target/game-desktop-1.0-SNAPSHOT-jar-with-dependencies.jar`.


## Run

From the root directory run:

```bash
java -jar desktop/target/game-desktop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Alternatively, simply double click the jar file in `desktop/target`.

## Test

To test run:

```bash
mvn clean test
```

## Documentation

JavaDoc can be created by running

```bash
mvn javadoc:javadoc
```

The documentation can then be found at `core/target/site/apidocs/index.html`.

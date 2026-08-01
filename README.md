# Git-Obsidian
Tool to list mergeRequest-threads in obsidian

# Execute
create a gradle shadowJar
java --enable-native-access=ALL-UNNAMED -jar build/libs/git-obsidian-1.0.SNAPSHOT-all.jar


# Install gitObs CLI locally

## 1. Build the fat JAR

Build a standalone JAR containing all dependencies:
```
    ./gradlew shadowJar
```

The generated JAR will be located in:
```
    build/libs/git-obsidian-<version>-all.jar
```

---

## 2. Install the application files

Create a directory for the application:
```
    mkdir -p ~/.local/share/gitObs
```
Copy the JAR and rename it:
```
    cp build/libs/git-obsidian-*-all.jar ~/.local/share/gitObs/gitObs.jar
```
The final location should be:
```
    ~/.local/share/gitObs/gitObs.jar
```
---

## 3. Create the CLI launcher

Create a launcher script:
```
    mkdir -p ~/.local/bin
    vim ~/.local/bin/gitObs
```
Add the following content:
```
    #!/bin/sh
    exec java --enable-native-access=ALL-UNNAMED \
      -jar "$HOME/.local/share/gitObs/gitObs.jar" "$@"
```
Make it executable:
```
    chmod +x ~/.local/bin/gitObs
```
---

## 4. Add ~/.local/bin to PATH

Add this to ~/.bashrc:
```
    export PATH="$HOME/.local/bin:$PATH"
```
Reload your shell:
```
    source ~/.bashrc
```
---

## 5. Test installation

Run:
```
    gitObs --help
```
Example output:
```
    Usage: gitObs [<options>] <command> [<args>]...

    Options:
      -h, --help  Show this message and exit

    Commands:
      resolve  Writes a interactive summary of unresolved Threads into your Obsidian Vault
      config   set configurations
```
---

## Updating gitObs

To update the CLI:

    ./gradlew shadowJar
    cp build/libs/git-obsidian-*-all.jar ~/.local/share/gitObs/gitObs.jar

The gitObs command remains unchanged.
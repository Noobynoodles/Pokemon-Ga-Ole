# Pokemon Ga-CLI

Java console game comprising the 18 uploaded Java source files. The game sources are unchanged. See BUG_REPORT.md before using the game: compilation passes, but runtime bugs remain.

## Requirements

JDK 17 or later, with `java` and `javac` on PATH. Run all commands from this project folder. The Java text blocks require Java 15 or later; Java 17 was used for verification.

## Run on Windows (PowerShell)

```powershell
New-Item -ItemType Directory -Force build, src/data
Copy-Item test-fixtures/pokemon.txt src/data/pokemon.txt
javac -d build src/*.java
java -cp build Main
```

The copied catalogue is synthetic test data, not the original assignment catalogue. Replace it with your actual `pokemon.txt` when available. Do not overwrite an existing catalogue without keeping a copy. `players.txt` and `leaderboard.txt` are optional on first run and are created when saving, provided `src/data` exists.

## Run on macOS/Linux

```bash
mkdir -p build src/data
cp test-fixtures/pokemon.txt src/data/pokemon.txt
javac -d build src/*.java
java -cp build Main
```

## Data format

`src/data/pokemon.txt` has one record per line, separated by comma and space:

```text
1, Charmander, 1, Fire, Test move, 100, 50, true, 100, 100, 50
```

Fields: collection number, name, grade, type, move, max health, offense, physical attack flag, defense, special defense, speed. Supported type subclasses: Fire, Water, Grass, Flying, Electric, Ground. Defense values must be positive. Current starter selection requires at least seven catalogue entries (indices 0, 3 and 6). Save loading currently assumes collection numbers match one-based row positions.

`src/data/players.txt` uses two lines per player: player name, followed by comma-and-space-separated collection numbers. `src/data/leaderboard.txt` uses `name, score` per line. Neither contains passwords; this is local save selection, not authenticated login.

## Reproduce the review

With Python 3 and a JDK:

```bash
python tests/check_original.py
```

The script compiles the original code, runs isolated subprocesses with synthetic data, and writes `tests/results.json`. Some subprocesses deliberately crash to demonstrate bugs; this is a diagnostic runner, not a passing regression test suite. No saved player data is required or modified. Fixtures and raw diagnostic results are included.

## Push to your GitHub

Create an empty repository on your GitHub account. Choose the name and visibility yourself; leave the README, .gitignore and licence options unticked because this project already supplies files. Then run from this folder:

```bash
git init -b main
git add .
git commit -m "Add Pokemon CLI game and diagnostic checks"
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

Replace the URL with your actual repository URL. If `origin` already exists, inspect `git remote -v` before changing it. If Git requests author details, set your own name and GitHub-associated email with `git config user.name` and `git config user.email`. Authenticate through your Git credential manager/browser. Do not put a password or token in project files. If the GitHub repository already has commits, clone it and copy these files into the clone rather than force-pushing.

The .gitignore excludes build products, local save files, environment files and IDE settings. The synthetic catalogue in test-fixtures is included intentionally. No licence is added: choose one if you want to grant reuse rights.

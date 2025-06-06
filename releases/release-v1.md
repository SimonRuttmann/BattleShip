## 🛠️ Release Notes – Version 1.0.0

We proudly present the first full release of **BattleShip** – a complete turn-based naval tactics game featuring AI, multiplayer, and full GUI support.

This release contains everything you need to play the game, save your progress, and review matches – whether locally or over the network.

---

### 📦 Included in `Game.zip`

| File / Folder                | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| `.multiplayerGames/`        | Saved game states from multiplayer sessions                                 |
| `.singleplayerGames/`       | Saved game states from singleplayer sessions                                |
| `.Logs/`                    | Log files for debugging and tracking game events (used with logging version)|
| `LinkSaveGames.txt`         | Internal file used to map multiplayer save slots                            |
| `Battleship.jar`            | The main game – launch this to start playing                                |
| `BattleshipWithLogging.jar`| Identical game version, but with console logging enabled                    |

> 🧩 Some folders (starting with a `.`) may be hidden by your OS – enable hidden files to view them.

---

### ▶️ How to Start

1. Ensure you have **Java 8+ with JavaFX** installed
   > ✅ Last tested with [Zulu JDK FX 24.30.13](https://www.azul.com/downloads/?os=windows&architecture=x86-64-bit&package=jdk-fx#zulu)
2. Run:
    - `Battleship.jar` to play normally
    - `BattleshipWithLogging.jar` to see logs in the console (for debugging or analysis)

---

### 🧪 Notes

- Multiplayer works over LAN or via [Hamachi](https://vpn.net/) for remote games.
- `LinkSaveGames.txt` should not be deleted – it ensures multiplayer save files are correctly linked.
- Save & Load is supported in both singleplayer and multiplayer modes.
- For more info, visuals, and animation previews, check the main [README](https://github.com/SimonRuttmann/BattleShip)
---

### 📜 License

This project is released under the [Apache 2.0 License](https://github.com/SimonRuttmann/BattleShip/blob/main/LICENSE).  
Feel free to use, modify, or share it – just give credit.

---

### 🎮 Download

👉 [Download Game.zip](https://github.com/SimonRuttmann/BattleShip/releases/download/v1.0.0/Game.zip)

---
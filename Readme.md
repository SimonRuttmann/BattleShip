# 🚢 BattleShip – Let the battle begin!

Welcome to our first fully realized game project: **BattleShip**!  
A turn-based tactics game for two players – either against a friend or a custom-built AI.

![Menu Screenshot](media/gifs/menu-singleplayer.gif)

This project marked our first dive into developing a complete software product.

---

## 🎓 Project & Context

This game was developed as part of our studies at **Aalen University** during the **"Programmierpraktikum"** (Programming Lab) course.  
It wasn't just about building a working game – we also gained first-hand experience in:

- **Project planning**
- **Team collaboration & roles**
- **Communication & code integration**
- And ultimately: **shipping** a playable game!

A unique aspect of the course was a shared **socket-based network protocol**.  
All teams implemented it individually, allowing us to compete across different codebases.  
This repository represents *our* take on the game.

---

## 🔧 Features

- 🌐 **Networked Multiplayer**
- 🤖 **Play against AI**
- 🎮 **Multiplayer** 
- 🎨 **Graphical User Interface**
- 💾 **Save & Load**
- 📊 **Flexible board sizes and ship counts**
- 🔄 **AI vs. AI mode**

---

## 🕹️ Gameplay Overview

### 1. Main Menu

Choose your game mode:

- **Singleplayer**
    - Play against the AI
    - Or let two AIs battle each other!

- **Multiplayer**
    - Join as host or client
    - Let an AI take control of your side

- **Options**
    - Adjust AI shooting speed, music volume, and language

![Menu Screenshot](media/gifs/menu.gif)

---

### 2. Game Setup

- Define grid size
- Choose ship types and quantities
- Set AI difficulty level

![Settings](media/gifs/singleplayer-gamesettings.gif)

---

### 3. Ship Placement

- Drag & drop ships onto your grid
- Or place them automatically
- Supports different ship counts and sizes

![Placement](media/gifs/singleplayer-ship-placement.gif)

---

### 4. Battle!

Play solo, multiplayer, or watch AI vs AI battles.

![Gameplay](media/gifs/singleplayer-player-vs-ai.gif)  
![Gameplay](media/gifs/singleplayer-huge-map-shooting.gif)

---

## 🎨 User Interface

- Built with JavaFX
- Responsive controls and inputs
- Extensively utilizing animations with sliders
- Drag & drop

| ![](media/img/menu.png)            | ![](media/img/load-save.png)             | ![](media/img/options.png) |
|------------------------------------|------------------------------------------|-----------------------------|
| ![](media/img/ingame-settings.png) | ![](media/img/ingame-ship-placement.png) | ![](media/img/ingame-shooting.png) |
| ![](media/img/ingame-saving.png)   | ![](media/img/multiplayer-client.png)    | ![](media/img/multiplayer-server.png) |

---

## 🌍 Multiplayer

Play locally or over via LAN.

You can use [Hamachi](https://vpn.net/) for remote connections.
We used it during the development with other battleship teams to play against each other and it worked just fine!


### Hosting a Game

- Choose map and ship settings
- Share your IP with your opponent

### Joining a Game

- Enter the host’s IP
- Choose to play as a human or let an AI play for you

Multiplayer also supports **saving and loading** games!

![Multiplayer Setup](media/gifs/multiplayer-connection.gif)  
Player vs Player
![Multiplayer PvP](media/gifs/multiplayer-player-vs-player-shooting.gif)  
AI vs AI
![Multiplayer AI vs AI](media/gifs/multiplayer-ai-vs-ai-shooting.gif)

> Thanks to a strict socket protocol, cheating is **not possible**.  
> Our hard AI has dominated **every** inter-team competition so far. ⚔️

---

## 🤖 AI System

The AI can be used in both Singleplayer and Multiplayer modes.

> You can even pit two AIs against each other and just watch!  
Perfect for comparing difficulty levels or discovering behavior patterns.

### Difficulty Levels
- **Normal** – Uses common sense at destroying ships but also shots random at times
- **Hard** – The hard one is as the name suggests tough

> We have implemented a **self-conceived backtracking algorithm** to identify and kill your ships - so watch out!

![AI vs AI](media/gifs/ai-vs-ai-challenge.gif)
---

## 💾 Save & Load

- Save at any time in both Singleplayer and Multiplayer
- Choose your save file name
- Resumes exactly where you left off
- Data is saved in `.json` format

![Saving](media/gifs/singleplayer-loading.gif)

---

## 💡 What We Learned

Throughout this project, we gained practical experience in:

- Structuring a mid-sized codebase
- Version control & merge strategies
- Working with JavaFX and event-driven UIs
- Implementing real-time communication via sockets
- Designing and maintaining a shared cross-team protocol

---

## 🧑‍💻 Teamwork Makes the Dream Work

Developed by:

- Simon Ruttmann
- Christian Schmidt
- Robin Röcker
- Yannick Söll

---

## 📜 License

This project is licensed under the [Apache 2.0 License](LICENSE). Feel free to use or remix it – just give proper credit. 🤝

---

## 📦 Downloads & Releases

Want to play?  
➡️ **[Click here to download the latest release](https://github.com/SimonRuttmann/BattleShip/releases/tag/v1.0.0)**

> **Requirements:**
> - Java > 1.8 with JavaFX
> - Last tested with [Zulu JDK FX 24.30.13](https://www.azul.com/downloads/?os=windows&architecture=x86-64-bit&package=jdk-fx#zulu)

---

## 🖼️ Videos

🎬 [Watch Singleplayer (MKV)](media/video/singleplayer.mkv)

🎬 [Watch Multiplayer (MKV)](media/video/multiplayer.mkv)

🎬 [Watch Singleplayer Huge Map (MKV)](media/video/ai-vs-ai-singleplayer.mkv)

🎬 [Watch AI vs AI Fastplay (MKV)](media/video/ai-vs-ai-german-fastplay.mkv)

🎬 [Watch AI vs AI Multiplayer (MKV)](media/video/ai-vs-ai-multiplayer.mkv)

🎬 [Watch AI vs AI Singleplayer (MKV)](media/video/ai-vs-ai-singleplayer.mkv)


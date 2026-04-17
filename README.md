# Gliby's Voice Chat [GTNH Edition]

[![Minecraft](https://img.shields.io/badge/Minecraft-1.7.10-green.svg)](https://minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-10.13.4.1614-orange.svg)](https://files.minecraftforge.net)

**Gliby's Voice Chat** is a Minecraft Forge mod that provides distance-based voice communication in-game.  
This is a community-maintained fork with modern build tooling and new features.

---

## Features

- **Proximity voice chat** with positional audio
- **Push-to-talk** and toggle-to-talk modes
- **Per-player volume control** (0–200%)
- **Player mute** functionality
- **Configurable microphone boost**
- **Customizable UI placement**
- Supports both **direct** and **global** voice modes

---

## What's New in This Fork

This fork brings the original Gliby's Voice Chat up to modern development standards:

- ✅ Migrated to the **GTNH build system** for easier maintenance and compatibility
- ✅ Added **per-player volume settings** with support for amplification above 100%
- ✅ Reworked **Player Settings** GUI for better control
- ✅ Real-time volume adjustment while other players are speaking
- ✅ Improved audio processing for louder playback when needed

See [CHANGELOG.md](CHANGELOG.md) for full version history.

---

## Installation

1. Download the latest release from the [Releases](../../releases) page.
2. Place the `.jar` file in your `.minecraft/mods` folder.
3. Launch Minecraft with Forge 1.7.10.

**Note:** Both server and client need the mod installed for voice chat to work.

---

## Usage

### Default Keybindings

| Action | Default Key |
|--------|-------------|
| Push-to-Talk | `V` |
| Open Settings | `.` (Period) |

You can rebind these in **Options → Controls → Multiplayer**.

### Player Settings

Press `.` (Period) to open the mod settings, then click **Player Settings** to:
- Adjust individual player volumes (0–200%)
- Mute/unmute specific players
- See all online players with voice chat enabled

---

## Building from Source

This project uses the [GTNH build system](https://github.com/GTNewHorizons/ExampleMod1.7.10).

### Requirements
- Java 8 or higher
- Git

### Build Steps

```bash
git clone https://github.com/Eldrinn-Elantey/VoiceChat.git
cd VoiceChat
./gradlew build

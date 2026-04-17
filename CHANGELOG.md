# Changelog

All notable changes to this project will be documented in this file.

## [0.8.1]

### Added
- Added toggle buttons in **UI Options** to show/hide:
  - Voice Icons (above player heads)
  - Voice Plates (speaker list in corner)
  - Speaking Indicator (microphone icon when talking)
- All HUD visibility settings are saved to config.

### Fixed
- Fixed accidental `super.onGuiClosed()` call in `updateScreen()`.

## [0.8.0]

### Added
- Added a new **Player Settings** screen for managing voice-related settings per player.
- Added **per-player volume control**.
- Added support for increasing player volume above 100%.

### Changed
- Reworked the old local mute screen into a full **Player Settings** screen.
- Player settings now list **all online players** instead of only manually added muted players.
- Player volume changes are saved in the client configuration file.
- Updated related GUI labels and localization entries.

### Fixed
- Fixed per-player volume updates so they apply to active voice streams in real time.
- Fixed player volume scaling so values above 100% now actually increase playback loudness.
- Fixed mute/player management behavior in the player settings screen.

## [0.7.0]

### Changed
- Migrated the project to the **GTNH build system**.
- Switched versioning to use **Git tags** as intended by the GTNH build setup.
- Updated resource processing and metadata templates for the new build environment.
- Adjusted project structure and build configuration for GTNH conventions.

## [0.6.2]

### Legacy
- Last upstream/original release before this fork.
- Baseline used for the GTNH migration and further development.

# Weather Forecast (Android)

A simple, elegant Android app that shows a **3-day weather forecast** for a selected city.
Built with **Kotlin**, **Jetpack Compose**, **Room**, and **Retrofit**.  
Supports **offline access**: last fetched forecast and local city suggestions are available without internet.

## Features
-  **City search** with suggestions (online + offline cache)
-  **3-day forecast**: date, min/max temperature, and condition icon
-  **Offline support**:
  - Forecast: last successful data is available offline
  - City suggestions: cached cities from successful searches
-  **Modern UI** with Material 3 and edge-to-edge, status bar safe insets

## Tech Stack
- Language: Kotlin (Compose)
- UI: Jetpack Compose, Material 3
- Data: Room (SQLite)
- Network: Retrofit + OkHttp
- Architecture: MVVM + Use Cases (Clean-ish layering)
- Min SDK 26, Compile/Target SDK 36


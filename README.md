# ⛅ WeatherApp

An elegant, modern Android application for discovering real-time weather, 10-day forecasts, and
detailed meteorological metrics. Built entirely with **Kotlin** and **Jetpack Compose**, following
the principles of **Clean Architecture**, **State-Driven Navigation**, and a **Reactive UI**
approach.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blue.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=android)
![Adaptive UI](https://img.shields.io/badge/Adaptive_UI-Foldables_%26_Tablets-8A2BE2.svg)
![Koin](https://img.shields.io/badge/DI-Koin-FF4154.svg)
![Ktor](https://img.shields.io/badge/Network-Ktor-087CFA.svg)
![Open-Meteo](https://img.shields.io/badge/API-Open--Meteo-2D3E50.svg)

---

## 📱 Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/6af57210-3706-4148-8430-aae3ae4a80bb" width="24%" />
  <img src="https://github.com/user-attachments/assets/27624b8b-1e72-4dd1-818d-29d3ddd04ec7" width="24%" />
  <img src="https://github.com/user-attachments/assets/66b775f5-d848-40fb-a09b-a3444217f291" width="24%" />
  <img src="https://github.com/user-attachments/assets/e062b59a-318e-4973-8290-85144646c31d" width="24%" />
<br><br>
  <img src="https://github.com/user-attachments/assets/1fd5d415-6ade-4952-9f0d-49cfbf0b4274" width="31%" />
  <img src="https://github.com/user-attachments/assets/13411671-cf76-415f-a6f9-ff48c7a44c11" width="31%" />
  <img src="https://github.com/user-attachments/assets/745c3a7b-96e4-4136-b859-11dce344ba12" width="31%" />
</div>

---

## ✨ Features

* **Advanced Data Visualization:** Features an Apple Weather-style dynamic temperature gradient bar
  that calculates weekly extremes and linearly interpolates colors for each specific day.
* **Smart City Search (Geocoding):** Real-time city search with debounce mechanism. Automatically
  maps country codes to native Unicode Emoji Flags (🇷🇺, 🇺🇸, 🇯🇵) without storing image assets.
* **Live Clock Synchronization:** Uses `kotlinx-datetime` and Coroutines to calculate the exact time
  in the selected city's timezone, updating seamlessly at the exact start of every new minute with
  zero CPU overhead.
* **Optimized Animations:** Uses the cutting-edge `.lottie` (dotLottie) format for weather icons,
  reducing asset size by 90%. Animations are GPU-accelerated (`RenderMode.HARDWARE`) to ensure 120
  FPS scrolling.
* **Adaptive UI (Tablets & Foldables):** Fully optimized for large screens using **Material 3
  Adaptive**. Custom liquid-style Bottom Navigation Bar drawn via pure Canvas/Path math that
  seamlessly adapts to Landscape and Foldable screens as a Side Navigation Rail.
* **State-Driven Navigation (Navigation 3):** Complete removal of `NavController` in favor of pure
  Kotlin State (`List<NavKey>`).
* **Reactive State Management:** Powered by `StateFlow` and `flatMapLatest`. Smart cancellation of
  outdated network requests and seamless state updates.
* **Dynamic Theme & Localization:** Seamless switching between Light/Dark themes and multiple
  languages on-the-fly using `DataStore` and `CompositionLocal`.

---

## 🛠 Tech Stack

* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Navigation:** Navigation 3
* **Architecture:** Strict Clean Architecture, Multi-module (UDF / MVI-like state management)
* **Dependency Injection:** [Koin](https://insert-koin.io/)
* **Networking:** [Ktor Client](https://ktor.io/) (with ContentNegotiation & Logging)
* **Preferences:
  ** [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
* **Time Management:** [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)
* **Animations:** [Lottie for Compose](https://airbnb.io/lottie/)
* **Weather API:** [Open-Meteo](https://open-meteo.com/) (Free, No API Key required)

---

## ⚙️ Project Architecture

The project is strictly modularized by layers, enforcing the Dependency Inversion Principle and
encapsulation (`internal` modifiers):

* `:domain` (Pure Kotlin): The heart of the app. Contains business models, abstract repository
  interfaces, and pure business logic (UseCases for calculating weather durations and extremes). Has
  zero Android dependencies.
* `:data` (Android Library): Handles remote data fetching via Ktor. Implements the `:domain`
  interfaces. Network DTOs and Mappers are kept `internal` to prevent leaking into the UI.
* `:ui` (Android Library): Contains Jetpack Compose screens, ViewModels, Custom Canvas Drawings, and
  UI-specific formatters. Depends only on `:domain`.
* `:app` (Android App): The lightweight shell (Entry Point). Connects all modules together,
  initializes Koin, and sets up the root `NavDisplay`.

---

## 🚀 Getting Started

This project uses the **Open-Meteo API**, which is completely free for non-commercial use and **does
not require an API key**!

You can build and run the project immediately:

1. Clone the repository:
   ```bash
   git clone https://github.com/RomanAisly/WeatherApp
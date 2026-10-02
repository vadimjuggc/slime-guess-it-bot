# 🎵 SlimeGuessItBot

![gif](https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExYnQ5OThrNDkyOGI4a3ZnM2YwbXJxeWxpMHd0d256dXRrcXowZXdyMCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RRg3SdtTzqtHMtzqPV/giphy.gif)

A Telegram bot for underground music lovers. Listen to a short audio clip and guess the track — if you know, you know.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Telegram](https://img.shields.io/badge/Telegram-Bot_API-2CA5E0?style=flat-square&logo=telegram)
![Maven](https://img.shields.io/badge/Maven-3.8-red?style=flat-square&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-In_Progress-yellow?style=flat-square)

---

## 🎮 How It Works

![gif](https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExZ2x2NmRsd2FtbWtmNXd6M24xc2VnaTQ1YmcwOGVxcXZ5aWVva3ltbSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/1tmJGGlnHFuKeCHSXe/giphy.gif)

1. Bot sends a short audio clip of a track
2. You pick the correct title from **4 options** via inline keyboard
3. Score and response time are tracked
4. Tracks cycle without repetition until all are played

---

## ✨ Features

- 🎧 Audio clip playback via Telegram
- ⌨️ Inline keyboard with 4 answer options
- 👤 Per-user session management
- ⏱️ Response time measurement
- 🏆 Leaderboard
- 🔄 No track repetition within a cycle

---

## 🏗️ Architecture

| Class | Responsibility |
|-------|---------------|
| `Main` | Entry point, bot initialization |
| `SlimeGuessItBot` | Core bot logic, update handling |
| `Track` | Track model (title, audio path) |
| `TrackDatabase` | Track storage, shuffle logic |

**Session management** — `HashMap<Long, UserSession>` by `chatId`  
**No repetition** — tracks are shuffled and cycled through completely before repeating

---

## 🚀 How to Run

**Requirements:** Java 17+, Maven, Telegram Bot

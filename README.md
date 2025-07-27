# 🐝 ZenHive – Your Social Audio Playground

ZenHive is a Kotlin + Firebase-powered mobile application where users can discover, join, and host live audio rooms—just like Clubhouse, but with a Gen-Z vibe. It offers real-time interaction, customizable profiles, and a hive-based content system designed to help people connect around shared interests.

---

## 🔍 Features

### 🔐 Authentication
•⁠  ⁠Secure login and signup via email/password or Google
•⁠  ⁠Phone number verification with OTP
•⁠  ⁠Persistent user sessions with SharedPreferences
•⁠  ⁠Firebase Authentication integration

### 🐝 Hive Features
•⁠  ⁠*Live Audio Rooms* – Join or host public group audio sessions
•⁠  ⁠*Create Hives* – Start and share topic-based conversations
•⁠  ⁠*Explore Page* – Discover trending and featured Hives
•⁠  ⁠*Join with a Tap* – Seamless audio room entry with mic permissions

### 👤 User Profile
•⁠  ⁠Custom avatars and display names
•⁠  ⁠Bio, interests, and social links (Instagram, Spotify)
•⁠  ⁠Created Hive history display
•⁠  ⁠Edit and persist profile via Firebase Realtime Database

---

##  Problem	✔️ Solution

| Problem | Solution |
|--------|----------|
| Users need a fun, voice-based way to connect | 🎤 ZenHive’s real-time Hives offer drop-in audio chats |
| Profiles feel generic in social apps | 🧬 Custom bios, socials, and interests bring personality |
| Audio rooms are hard to discover | 🌍 Featured Hives and Explore page solve discovery |
| Hosting rooms is too complex | ➕ One-tap Hive creation with simple interface |
| Data doesn’t persist after login | 💾 Smart use of SharedPreferences + Firebase ensures continuity |

---

## 🛠️ Tech Stack

### 📦 Core Technologies
•⁠  ⁠*Kotlin* – Primary language
•⁠  ⁠*Jetpack Compose* – Declarative UI toolkit
•⁠  ⁠*ZegoCloud* – Real-time audio communication SDK
•⁠  ⁠*Coil* – Image loading
•⁠  ⁠*Coroutines + Flow* – Asynchronous logic

### 🔥 Firebase Services
•⁠  ⁠*Authentication* – User sign-in & session management
•⁠  ⁠*Realtime Database* – Hive and user data storage
•⁠  ⁠*Cloud Storage* – For profile images via Cloudinary
•⁠  ⁠*Firebase Messaging (planned)* – Push notifications

---

## 📁 Project Structure (MVVM)


app/src/main/java/com/example/zenhive/
├── model/            # Data models (UserModel, HiveModel)
├── repository/       # Firebase interaction logic
├── viewmodel/        # ViewModels with state handling
├── view/             # Jetpack Compose UI files
├── utils/            # Helper functions and constants
├── navigation/       # Navigation logic between screens


---

## 👥 Team

| Name | Role |
|------|------|
| Sushil Neupane | Frontend Developer and Scrum Master |
| Sabin Raj Pokharel | Backend Developer, Database Developer and Project Planner |
| Gaurav Giri | UI/UX and Development |
| Shreeshubh Thapa | Design and Frontend Implementation |
| Deeya Lacoul | Testing and Database Design |

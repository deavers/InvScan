# 📦 Inventory Scanner — Android Mobile Application

> A mobile application for scanning, recognizing, and counting inventory objects using AI (Google ML Kit) and a REST API backend.

---

## 📖 About the Project

**Inventory Scanner** is an Android application developed as a diploma project at Ust-Kamenogorsk Higher Polytechnic College (2022). The application is designed for employees of organizations (warehouses, schools, enterprises) who need to conduct asset inventory efficiently.

Instead of manually typing inventory numbers, users can **photograph or scan** an item's inventory tag — the AI automatically recognizes the number and searches it against a remote database, then generates an inventory report.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏫 Room Selection | Choose a classroom or department from the server database |
| 🔍 Smart Search | Live search by inventory number within a room or across the global database |
| 📷 AI Scanner | Recognize inventory numbers from a photo (camera or gallery) |
| ✅ Object Marking | Mark found items as checked during the inventory session |
| 📊 Report Generation | Generate and save an inventory report to device storage |
| 🌐 REST API Sync | All data is synced with a remote MariaDB server via HTTP |

---

## 🏗️ Architecture

The application uses a **2-tier client-server architecture**:

```
┌────────────────────┐         HTTP (REST API)        ┌─────────────────────┐
│   Android Client   │  ──────────────────────────►  │    Remote Server     │
│  (Kotlin + ML Kit) │  ◄──────────────────────────  │  MariaDB + PHP API  │
└────────────────────┘                                └─────────────────────┘
```

- **Client**: Android app (Kotlin, XML layouts)
- **Server**: Hosted on [Gohost.kz](https://gohost.kz), database managed via phpMyAdmin
- **Communication**: REST API using HTTP methods (GET, POST, PUT, DELETE)
- **AI**: Google ML Kit (on-device text recognition, no cloud dependency for basic scanning)

---

## 🗄️ Database Schema (3NF)

```
Classrooms          Categories          Items
──────────────      ──────────────      ──────────────────────────
num (PK)            id (PK)             inventory_num (PK)
name                name                name
                                        category_id (FK → Categories)
                                        class_room_num (FK → Classrooms)
                                        created_at
                                        updated_at
```

---

## 📱 Screens & Navigation

```
SplashScreen
    │  (permission check + internet check)
    ▼
MainActivity (Home)
    │  BottomNav: [🏠 Home] [🔍 Search]
    ├── HomeFragment — classroom list from server
    │       │
    │       ▼
    │   SearchFragment — items list for selected room
    │       ├── 🔍 Live search (room → global DB)
    │       ├── 📷 Scanner → ScannerActivity
    │       └── 📊 Report → CartListActivity
    │
    └── ScannerActivity — camera / gallery + crop + ML Kit OCR
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin (primary), Java, XML |
| **IDE** | Android Studio |
| **AI / ML** | Google ML Kit — Text Recognition |
| **Networking** | REST API over HTTP/HTTPS |
| **Database** | MariaDB (remote), phpMyAdmin |
| **Architecture** | 2-Tier Client-Server, MVVM (ViewModel) |
| **Min SDK** | Android 8.0 (API Level 26) |

---

## 📋 System Requirements

### Device Requirements

| Parameter | Minimum |
|---|---|
| Android version | 8.0 (Oreo) or higher — API Level 26 |
| RAM | 1.5 GB or more |
| Free storage | 800 MB or more |
| Screen resolution | 1080 × 1920 px (FHD) |
| CPU frequency | 1.6 GHz or higher |
| CPU cores | 2 or more |
| Internet | Required (for DB sync and ML Kit cloud fallback) |

### Permissions Required

- `CAMERA` — for scanning inventory tags
- `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` — for gallery access and report saving
- `INTERNET` — for REST API communication

---

## 📂 Project Structure

```
app/
├── manifests/
│   └── AndroidManifest.xml          # App config, permissions, components
├── java/
│   ├── MainActivity.kt              # Main entry screen
│   ├── SplashScreen.kt             # Splash + permission check
│   ├── ScannerActivity.kt          # Camera/gallery scan + ML Kit OCR
│   ├── CartListActivity.kt         # Report screen (marked items)
│   ├── HomeFragment.kt             # Classroom list fragment
│   ├── SearchFragment.kt           # Item search + scan trigger
│   ├── ConnectionDetector.kt       # Internet connectivity check
│   ├── HomeViewModel.kt            # ViewModel for HomeFragment
│   ├── ScanViewModel.kt            # ViewModel for ScannerActivity
│   ├── SearchViewModel.kt          # ViewModel for SearchFragment
│   ├── adapters/                   # RecyclerView adapters
│   ├── data/                       # REST API (Retrofit/HTTP) calls
│   ├── domain/                     # Data models (Classroom, Item, Category)
│   ├── interfaces/                 # Click listeners, UI interfaces
│   └── utils/                      # Constants and utility functions
└── res/
    ├── layout/                     # XML screens (6 screens + templates)
    ├── drawable/                   # Icons, vector graphics
    ├── font/                       # 4 custom fonts
    ├── mipmap/                     # App launcher icons
    ├── values/                     # Colors, themes, strings
    └── xml/                        # Backup rules, file paths
```

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/deavers/InvScan.git
cd InvScan
```

### 2. Open in Android Studio

Open the project folder in **Android Studio** (Flamingo or newer recommended).

### 3. Configure the API endpoint

In `utils/Constants.kt` (or equivalent), set your server base URL:

```kotlin
const val BASE_URL = "https://your-server.com/api/"
```

### 4. Set up the database

Import the SQL schema into your MariaDB server using phpMyAdmin:

```sql
CREATE TABLE classrooms (num VARCHAR(20) PRIMARY KEY, name VARCHAR(100));
CREATE TABLE categories (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100));
CREATE TABLE items (
    inventory_num VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200),
    category_id INT,
    class_room_num VARCHAR(20),
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (class_room_num) REFERENCES classrooms(num)
);
```

### 5. Build & Run

```bash
# Via Android Studio: Run ▶ or
./gradlew assembleDebug
```

---

## 🧠 How ML Kit Text Recognition Works

1. User takes a photo or selects from gallery
2. The image is **cropped** to the region of interest
3. **Google ML Kit** processes the image on-device:
   - If confidence ≥ 70% → result accepted
   - If confidence < 70% → fallback to Google Cloud ML Kit
   - If still < 70% → closest match is used
4. Recognized number is searched in the room DB, then the global DB
5. Result is displayed with item info and category

Average recognition time: **~1 second** (varies by device and image quality)

---

## 📄 Report Format

Generated reports are saved to device storage as files named:

```
[RoomName]_[YYYY-MM-DD].xlsx / .txt
```

Report contents:
- Room full name
- Total items in room
- Number of marked (checked) items
- All inventory numbers with ✅ / ❌ status per item

---

## 🔌 REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/classrooms` | Get all classrooms |
| GET | `/items?room={num}` | Get items for a specific room |
| GET | `/items/{inventory_num}` | Search item globally |
| POST | `/items` | Add new item |
| PUT | `/items/{inventory_num}` | Move item to another room |
| DELETE | `/items/{inventory_num}` | Remove item |

---

## 👤 Author

**Sairakhan Shynggys**
Diploma Project — Ust-Kamenogorsk Higher Polytechnic College  
Specialty: Computer Engineering and Software  
Group: 18-VT-2 | Year: 2022

---

## 📜 License

This project was developed as an educational diploma project.  
For reuse or commercial deployment, please contact the author.

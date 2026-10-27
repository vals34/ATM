# All Tools Modern – Version Kotlin + Jetpack Compose

Recréation moderne et propre des fonctionnalités principales de **All Tools** (OCR, QR/Barcode, Notes, STT/TTS, Magnétophone, IP CCTV, Bluetooth, IR Remote).

## Stack technique

- **Kotlin** + **Jetpack Compose** (Material 3)
- **CameraX** + **ML Kit** (OCR + Barcode Scanning)
- **ZXing** (génération QR)
- **Room** (Notes)
- **SpeechRecognizer** + **TextToSpeech** (STT/TTS améliorés)
- **MediaRecorder** (Magnétophone haute qualité AAC)
- **ConsumerIrManager** + **BluetoothAdapter** (IR + Bluetooth)
- Architecture feature-based, permissions Accompanist, minSdk 26, targetSdk 35

## Fonctionnalités livrées

| Outil              | Description / Améliorations |
|--------------------|-----------------------------|
| **OCR Intelligent** | ML Kit Text Recognition + capture CameraX, copie presse-papiers |
| **QR & Barcode**   | Scanner temps réel ML Kit + générateur ZXing |
| **Notes**          | Room + CRUD complet, dates, suppression |
| **Speech to Text** | SpeechRecognizer avec résultats partiels, fr-FR, auto-feedback |
| **Text to Speech** | TTS système + contrôle vitesse/tonalité |
| **Magnétophone**   | AAC 44.1 kHz / 128 kbps, liste + lecture + suppression |
| **IP CCTV**        | Preview CameraX + modes Serveur/Client (base pour WebRTC) |
| **Bluetooth**      | Scan moderne Android 12+, liste live, base SPP/GATT |
| **IR Remote**      | ConsumerIrManager + patterns NEC (Power / Vol±) |

## Comment ouvrir et builder

1. Ouvrez le dossier `AllToolsModern` dans **Android Studio** (Ladybug ou plus récent recommandé).
2. Laissez Gradle synchroniser (les dépendances se téléchargent automatiquement).
3. Connectez un appareil ou lancez un émulateur (API 26+).
4. Cliquez sur **Run**.

### Permissions demandées à l’exécution
- Caméra (OCR, QR, CCTV)
- Micro (STT, Magnétophone, CCTV)
- Bluetooth / Localisation (scan Bluetooth)
- IR (automatique si matériel présent)

## Prochaines améliorations possibles

- WebRTC complet pour IP CCTV (stream P2P stable)
- Profils Bluetooth HID / SPP pour contrôle réel d’appareils
- Apprentissage de codes IR (capture + base de données)
- STT offline (Vosk / Whisper ONNX) pour mode 100 % hors-ligne
- Thème dynamique + dark mode parfait
- Export notes / enregistrements

## Structure du projet

```
app/src/main/java/com/alltools/modern/
├── AllToolsApp.kt
├── MainActivity.kt
├── ui/                 # Navigation + Home + Theme
├── data/               # Room (Notes)
└── features/
    ├── ocr/
    ├── qr/
    ├── notes/
    ├── stt/
    ├── tts/
    ├── recorder/
    ├── cctv/
    ├── bluetooth/
    └── ir/
```

Développé pour répondre à la demande : **Kotlin**, OCR plus efficace, QR/Barcode, Notes, meilleur STT/TTS, Magnétophone, IP CCTV et Bluetooth+IR améliorés.

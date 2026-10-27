package com.alltools.modern.features.tts

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TtsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("Bonjour, ceci est un test de synthèse vocale moderne.") }
    var isSpeaking by remember { mutableStateOf(false) }
    var pitch by remember { mutableFloatStateOf(1.0f) }
    var speed by remember { mutableFloatStateOf(1.0f) }
    var status by remember { mutableStateOf("Initialisation…") }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val engine = TextToSpeech(context) { result ->
            if (result == TextToSpeech.SUCCESS) {
                val langResult = engine.setLanguage(Locale.FRANCE)
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    status = "Langue FR non supportée – utilisation de la langue par défaut"
                    engine.language = Locale.getDefault()
                } else {
                    status = "Prêt (voix française)"
                }
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        isSpeaking = true
                        status = "Lecture en cours…"
                    }

                    override fun onDone(utteranceId: String?) {
                        isSpeaking = false
                        status = "Terminé"
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        isSpeaking = false
                        status = "Erreur de lecture"
                    }
                })
            } else {
                status = "Échec d’initialisation TTS"
                Toast.makeText(context, "TTS non disponible", Toast.LENGTH_LONG).show()
            }
        }
        tts = engine

        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text to Speech") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(status, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Texte à lire") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 6
            )

            Spacer(Modifier.height(16.dp))

            Text("Vitesse : ${"%.1f".format(speed)}x")
            Slider(
                value = speed,
                onValueChange = {
                    speed = it
                    tts?.setSpeechRate(it)
                },
                valueRange = 0.5f..2.0f
            )

            Text("Tonalité : ${"%.1f".format(pitch)}")
            Slider(
                value = pitch,
                onValueChange = {
                    pitch = it
                    tts?.setPitch(it)
                },
                valueRange = 0.5f..2.0f
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isSpeaking) {
                        tts?.stop()
                        isSpeaking = false
                        status = "Arrêté"
                    } else if (text.isNotBlank()) {
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utt1")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = tts != null && text.isNotBlank()
            ) {
                Icon(
                    if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(Modifier.padding(4.dp))
                Text(if (isSpeaking) "Arrêter" else "Lire le texte")
            }
        }
    }
}

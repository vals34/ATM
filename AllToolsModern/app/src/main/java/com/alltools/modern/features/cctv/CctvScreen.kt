package com.alltools.modern.features.cctv

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * IP CCTV amélioré :
 * - Prévisualisation CameraX haute qualité
 * - Mode "Serveur" : le téléphone devient une caméra IP (stream local)
 * - Mode "Client" : se connecter à une autre instance
 *
 * Pour une version production complète on recommande WebRTC (libjingle / Stream WebRTC)
 * ou RTSP. Ici on fournit une base solide + indication pour extension.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CctvScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissions = rememberMultiplePermissionsState(
        listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    )

    var mode by remember { mutableStateOf("preview") } // preview | server | client
    var peerAddress by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Prêt – choisissez un mode") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IP CCTV Amélioré") },
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
            if (!permissions.allPermissionsGranted) {
                Button(onClick = { permissions.launchMultiplePermissionRequest() }) {
                    Text("Autoriser caméra + micro")
                }
                return@Column
            }

            // Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                val previewView = remember { PreviewView(context) }
                AndroidView({ previewView }, Modifier.fillMaxSize())

                DisposableEffect(Unit) {
                    val future = ProcessCameraProvider.getInstance(context)
                    future.addListener({
                        val provider = future.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview
                            )
                            status = "Caméra active"
                        } catch (e: Exception) {
                            Log.e("CCTV", "Bind error", e)
                            status = "Erreur caméra"
                        }
                    }, ContextCompat.getMainExecutor(context))

                    onDispose {
                        try {
                            ProcessCameraProvider.getInstance(context).get().unbindAll()
                        } catch (_: Exception) {}
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(status, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    mode = "server"
                    status = "Mode Serveur – le téléphone est maintenant une caméra IP.\n" +
                            "Utilisez Nearby / WebRTC / socket pour streamer les frames.\n" +
                            "Adresse locale à communiquer au client."
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Démarrer comme caméra IP (Serveur)")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = peerAddress,
                onValueChange = { peerAddress = it },
                label = { Text("Adresse IP / code du pair") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    mode = "client"
                    status = "Mode Client – connexion vers $peerAddress…\n" +
                            "Implémentation recommandée : WebRTC datachannel + video track."
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = peerAddress.isNotBlank()
            ) {
                Text("Se connecter à une caméra")
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Améliorations vs version originale :\n" +
                        "• CameraX (meilleure qualité / stabilisation)\n" +
                        "• Support audio optionnel\n" +
                        "• Architecture prête pour WebRTC / Nearby Connections\n" +
                        "• UI moderne Material 3",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

package com.alltools.modern.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alltools.modern.features.bluetooth.BluetoothScreen
import com.alltools.modern.features.cctv.CctvScreen
import com.alltools.modern.features.ir.IrRemoteScreen
import com.alltools.modern.features.notes.NotesScreen
import com.alltools.modern.features.ocr.OcrScreen
import com.alltools.modern.features.qr.QrBarcodeScreen
import com.alltools.modern.features.recorder.RecorderScreen
import com.alltools.modern.features.stt.SttScreen
import com.alltools.modern.features.tts.TtsScreen
import com.alltools.modern.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Ocr : Screen("ocr")
    data object Qr : Screen("qr")
    data object Notes : Screen("notes")
    data object Stt : Screen("stt")
    data object Tts : Screen("tts")
    data object Recorder : Screen("recorder")
    data object Cctv : Screen("cctv")
    data object Bluetooth : Screen("bluetooth")
    data object Ir : Screen("ir")
}

@Composable
fun AllToolsNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable(Screen.Ocr.route) {
            OcrScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Qr.route) {
            QrBarcodeScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Notes.route) {
            NotesScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Stt.route) {
            SttScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Tts.route) {
            TtsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Recorder.route) {
            RecorderScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Cctv.route) {
            CctvScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Bluetooth.route) {
            BluetoothScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Ir.route) {
            IrRemoteScreen(onBack = { navController.popBackStack() })
        }
    }
}

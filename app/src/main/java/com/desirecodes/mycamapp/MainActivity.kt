package com.desirecodes.mycamapp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            checkAllFilesAccess()
        } else {
            showToast("Permissions denied. Camera/Location features may not work.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            setShowWhenLocked(true)
//            setTurnScreenOn(true)
//        } else {
//            window.addFlags(
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//            )
//        }
        checkAndRequestPermissions()
        startCameraService("")


        findViewById<Button>(R.id.btnStartService).setOnClickListener {
            if (hasRequiredCameraLocationPermissions()) {
                startCameraService("")
            } else {
                requestPermissionsLauncher.launch(requiredPermissions)
            }
        }

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            if (hasRequiredCameraLocationPermissions() && hasAllFilesAccess()) {
                startCameraService(CameraService.ACTION_START_VIDEO)
            } else {
                checkAndRequestPermissions()
            }
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            startCameraService(CameraService.ACTION_STOP_VIDEO)
        }

        findViewById<Button>(R.id.btnPhoto).setOnClickListener {
            if (hasRequiredCameraLocationPermissions() && hasAllFilesAccess()) {
                startCameraService(CameraService.ACTION_TAKE_PHOTO)
            } else {
                checkAndRequestPermissions()
            }
        }

        findViewById<Button>(R.id.btnFullStop).setOnClickListener {
            startCameraService(CameraService.ACTION_STOP_ALL)
        }
    }

    private fun checkAndRequestPermissions() {
        if (!hasRequiredCameraLocationPermissions()) {
            requestPermissionsLauncher.launch(requiredPermissions)
        } else {
            checkAllFilesAccess()
        }

        if (!hasOverlayPermission()) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun checkAllFilesAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                showToast("Please allow 'All Files Access' for custom hidden folder")
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
        }
    }

    private fun hasAllFilesAccess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }

    private fun startCameraService(action: String) {
        val intent = Intent(this, CameraService::class.java).apply {
            this.action = action
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}

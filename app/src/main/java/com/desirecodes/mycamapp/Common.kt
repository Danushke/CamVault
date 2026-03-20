package com.desirecodes.mycamapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(permissions: Array<String>): Boolean {
    return permissions.all { hasPermission(it) }
}

fun Context.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun Context.hasRequiredCameraLocationPermissions(): Boolean {
    val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    return hasPermissions(permissions.toTypedArray())
}

fun Context.hasLocationPermissions(): Boolean {
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    return hasPermissions(permissions.toTypedArray())
}

fun Context.hasRequiredCameraPermissions2(): Boolean {
    val permissions = mutableListOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    return hasPermissions(permissions.toTypedArray())
}

object Constant{
    const val ACTION_START_VIDEO = "com.desirecodes.mycamapp.START_VIDEO"
    const val FILE_NAME_FORMAT = "yyyyMMdd_HHmmss"


}

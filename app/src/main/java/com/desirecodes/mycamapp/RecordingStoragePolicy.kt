package com.desirecodes.mycamapp

internal object RecordingStoragePolicy {
    private const val MINIMUM_FREE_BYTES = 250L * 1024 * 1024

    fun hasEnoughSpace(availableBytes: Long): Boolean {
        return availableBytes >= MINIMUM_FREE_BYTES
    }
}

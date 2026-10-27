package com.desirecodes.mycamapp

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecordingStoragePolicyTest {
    @Test
    fun rejectsFullStorage() {
        assertFalse(RecordingStoragePolicy.hasEnoughSpace(0L))
    }

    @Test
    fun rejectsSpaceOneByteBelowMinimum() {
        assertFalse(RecordingStoragePolicy.hasEnoughSpace(250L * 1024 * 1024 - 1))
    }

    @Test
    fun acceptsExactlyMinimumSpace() {
        assertTrue(RecordingStoragePolicy.hasEnoughSpace(250L * 1024 * 1024))
    }

    @Test
    fun acceptsSpaceAboveMinimum() {
        assertTrue(RecordingStoragePolicy.hasEnoughSpace(250L * 1024 * 1024 + 1))
    }
}

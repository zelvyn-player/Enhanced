package com.zelvyn.player.translation

import com.zelvyn.player.model.TranslationSegment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong

class TranslationCoordinator(
    private val scope: CoroutineScope
) {
    private val currentSessionId = AtomicLong(0)

    private val _activeSegment = MutableStateFlow<TranslationSegment?>(null)
    val activeSegment: StateFlow<TranslationSegment?> = _activeSegment.asStateFlow()

    private val _isTranslationActive = MutableStateFlow(false)
    val isTranslationActive: StateFlow<Boolean> = _isTranslationActive.asStateFlow()

    private val segmentCache = mutableListOf<TranslationSegment>()

    fun startSession(sourceLang: String, targetLang: String): Long {
        val newSession = currentSessionId.incrementAndGet()
        segmentCache.clear()
        _activeSegment.value = null
        _isTranslationActive.value = true
        return newSession
    }

    fun stopTranslation() {
        currentSessionId.incrementAndGet()
        segmentCache.clear()
        _activeSegment.value = null
        _isTranslationActive.value = false
    }

    fun invalidateSession() {
        currentSessionId.incrementAndGet()
        segmentCache.clear()
        _activeSegment.value = null
    }

    fun onPlaybackPositionChanged(positionMs: Long) {
        val match = segmentCache.find { positionMs in it.startTimeMs..it.endTimeMs }
        _activeSegment.value = match
    }

    fun pushSegment(segment: TranslationSegment) {
        if (segment.sessionId == currentSessionId.get()) {
            segmentCache.add(segment)
        }
    }
}

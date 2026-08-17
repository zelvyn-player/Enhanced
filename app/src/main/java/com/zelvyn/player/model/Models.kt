package com.zelvyn.player.model

import android.net.Uri

data class VideoMediaItem(
    val id: Long,
    val uri: Uri,
    val title: String,
    val durationMs: Long,
    val resolution: String = "4K HDR",
    val lastPositionMs: Long = 0L
)

data class TranslationSegment(
    val sessionId: Long,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val originalText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String
)

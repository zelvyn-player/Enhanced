package com.zelvyn.player

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.zelvyn.player.model.TranslationSegment
import com.zelvyn.player.translation.TranslationCoordinator
import com.zelvyn.player.ui.player.PlayerScreen
import com.zelvyn.player.ui.theme.ZelvynColorScheme

class WatchActivity : ComponentActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private val translationCoordinator = TranslationCoordinator(lifecycleScope)

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.systemBars())

        exoPlayer = ExoPlayer.Builder(this).build().apply {
            val videoUri = intent.data ?: Uri.parse("https://storage.googleapis.com/exoplayer-test-media-1/mp4/dizzy-short.mp4")
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }

        setContent {
            MaterialTheme(colorScheme = ZelvynColorScheme) {
                val activeSegment by translationCoordinator.activeSegment.collectAsState()

                PlayerScreen(
                    exoPlayer = exoPlayer,
                    videoTitle = intent.getStringExtra("video_title") ?: "Interstellar (2014)",
                    activeSegment = activeSegment,
                    onBackPress = { finish() },
                    onOpenTranslationPanel = {
                        val sessionId = translationCoordinator.startSession("en", "es")
                        translationCoordinator.pushSegment(
                            TranslationSegment(
                                sessionId = sessionId,
                                startTimeMs = 0,
                                endTimeMs = 15000,
                                originalText = "Mankind was born on Earth. It was never meant to die here.",
                                translatedText = "La humanidad nació en la Tierra. Nunca fue meant para morir aquí.",
                                sourceLang = "en",
                                targetLang = "es"
                            )
                        )
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::exoPlayer.isInitialized) exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        if (::exoPlayer.isInitialized) exoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}

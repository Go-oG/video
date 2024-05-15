package com.goog.videodemo

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.goog.video.view.surface.GLSurfaceView2

class MainActivity : AppCompatActivity() {
    private lateinit var listView: RecyclerView
    private lateinit var videoView: GLSurfaceView2
    private lateinit var player: PlayerWrapper
    private lateinit var adapter: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listView = findViewById(R.id.listView)
        videoView = findViewById(R.id.videoView)

        findViewById<View>(R.id.playBTN).setOnClickListener {
            play()
        }
        findViewById<View>(R.id.pauseBTN).setOnClickListener {
            pause()
        }

        initPlayer()
        initListView()
    }

    private fun initPlayer() {
        val exoPlayer = ExoPlayer.Builder(this).build()
        player = PlayerWrapper(exoPlayer)
        findViewById<GLSurfaceView2>(R.id.videoView).setPlayer(player)
    }

    private fun initListView() {
        adapter = RVAdapter(this)
        adapter.clickListener = {
            val data = adapter.dataList[it]
            videoView.setGlFilter(data.builder.invoke())
        }
        adapter.dataList.addAll(FilterItem.loadFiltersData(this))
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        play()
    }

    override fun onStop() {
        super.onStop()
        pause()
    }

    private fun play() {
        pause()
        val url = "asset:///testVideo.mp4"
        val mediaItem = MediaItem.Builder().setUri(url)
            .build()
        player.player.repeatMode = Player.REPEAT_MODE_ONE
        player.player.setMediaItem(mediaItem)
        player.player.playWhenReady = true
        player.player.prepare()
    }

    private fun pause() {
        player.player.stop()
    }
}
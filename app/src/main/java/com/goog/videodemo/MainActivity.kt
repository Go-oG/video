package com.goog.videodemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goog.effect.view.surface.GLSurfaceView2
import com.goog.videodemo.adapter.FilterAdapter
import com.goog.videodemo.adapter.SeekAdapter
import com.goog.videodemo.data.FilterItem

@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity() {
    private lateinit var listView: RecyclerView
    private lateinit var videoView: GLSurfaceView2
    private lateinit var player: PlayerWrapper
    private lateinit var adapter: FilterAdapter
    private lateinit var seekAdapter: SeekAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        videoView = findViewById(R.id.videoView)

        findViewById<View>(R.id.playBtn).setOnClickListener {
            play()
        }
        findViewById<View>(R.id.pauseBtn).setOnClickListener {
            pause()
        }
        findViewById<View>(R.id.resetBtn).setOnClickListener {
            clearFilter()
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
        seekAdapter = SeekAdapter(this, null)
        val seekList = findViewById<RecyclerView>(R.id.seekList)
        seekList.adapter = seekAdapter
        seekList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = FilterAdapter(this)
        adapter.clickListener = {
            val data = adapter.dataList[it]
            if (!data.select) {
                var selectData: FilterItem? = null
                for ((index, item) in adapter.dataList.withIndex()) {
                    item.unselect()
                    if (index == it) {
                        selectData = item
                    }
                }
                selectData?.select()
                seekAdapter.changeFilter(selectData)
                adapter.notifyDataSetChanged()
                videoView.setGlFilter(selectData?.filter)
            }
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

    private fun clearFilter() {
        videoView.setGlFilter(null)
        seekAdapter.changeFilter(null)
        for (item in adapter.dataList) {
            item.unselect()
        }
        adapter.notifyDataSetChanged()
    }

}
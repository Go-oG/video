package com.goog.effect.view

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.goog.effect.Player

///用于辅助在View pager或者 RecyclerView中进行视频的播放
abstract class VideoHelper(val viewHelper: VideoViewHelper) {
    companion object {
        fun bindRecyclerView(view: RecyclerView, viewHelper: VideoViewHelper): VideoHelper {
            return RecyclerViewVideoHelper(view, viewHelper)
        }

        fun bindViewpagerView(view: ViewPager2, viewHelper: VideoViewHelper): VideoHelper {
            return ViewpagerVideoHelper(view, viewHelper)
        }
    }

    protected var player: Player
    protected var videoView: View

    init {
        var pair = viewHelper.buildVideoView()
        videoView = pair.first
        player = pair.second
    }

    open fun dispose() {}

    protected open fun stopPlay() {
        viewHelper.onShouldStopPlay()
        val parent = videoView.parent
        if (parent is ViewGroup) {
            parent.removeView(videoView)
        }
    }

    protected open fun handlePageSelected(position: Int): Boolean {
        stopPlay()
        var data = viewHelper.getVideoData(position)
        if (data == null) {
            return false
        }
        var container = viewHelper.getVideoContainer(position)
        if (container == null) {
            return false
        }
        container.addView(videoView, -1, -1)
        viewHelper.onShouldPlayVideo(data)
        return true
    }

}

private class ViewpagerVideoHelper(private val view: ViewPager2, viewHelper: VideoViewHelper) : VideoHelper(viewHelper) {

    private val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            handlePageSelected(position)
        }
    }

    init {
        view.registerOnPageChangeCallback(listener)
    }

    override fun dispose() {
        super.dispose()
        view.unregisterOnPageChangeCallback(listener)
    }

}

private class RecyclerViewVideoHelper(private val view: RecyclerView, viewHelper: VideoViewHelper) : VideoHelper(viewHelper) {
    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            handleOnScrolled(recyclerView, dx, dy)

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            handleScrollStateChanged(recyclerView, newState)
        }
    }
    private val adapterObs=object :AdapterDataObserver(){
        override fun onChanged() {

        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {

        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {

        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {

        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {

        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {

        }
    }

    init {
        view.addOnScrollListener(listener)
        var adapter = view.adapter
        if (adapter == null) {
            throw IllegalAccessException("请在设置适配器后再调用该方法")
        }
        adapter.registerAdapterDataObserver(adapterObs)
    }

    override fun dispose() {
        super.dispose()
        view.removeOnScrollListener(listener)
        view.adapter?.unregisterAdapterDataObserver(adapterObs)
    }

    private var lastPlayPosition = -1

    private fun handleOnScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val oldPos = lastPlayPosition
        if (oldPos == -1) {
            return
        }

        var layoutManager = recyclerView.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            return
        }

        val firstPos = layoutManager.findFirstVisibleItemPosition()
        val lastPos = layoutManager.findLastVisibleItemPosition()
        if (oldPos >= firstPos && oldPos <= lastPos) {
            ///当前在屏幕中不处理
            return
        }
        lastPlayPosition = -1
        stopPlay()
    }

    private fun handleScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE || lastPlayPosition != -1) {
            return
        }
        var layoutManager = recyclerView.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            return
        }

        val firstPos = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastPos = layoutManager.findLastVisibleItemPosition()

        var index = -1
        index = if (lastPos == firstPos) {
            lastPos
        } else {
            if (firstPos == -1) {
                lastPos
            } else if (lastPos == -1) {
                firstPos
            } else {
                (lastPos + firstPos) / 2
            }
        }
        if (index != -1 && handlePageSelected(index)) {
            lastPlayPosition = index
        }
    }

}


interface VideoViewHelper {

    fun buildVideoView(): Pair<View, Player>

    fun getVideoData(position: Int): Uri?

    fun getVideoContainer(position: Int): FrameLayout?

    fun onShouldPlayVideo(uri: Uri)

    fun onShouldStopPlay()

}
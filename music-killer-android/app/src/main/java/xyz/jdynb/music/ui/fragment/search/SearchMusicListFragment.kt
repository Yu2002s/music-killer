package xyz.jdynb.music.ui.fragment.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.net.Get
import com.drake.net.utils.scope
import kotlinx.coroutines.launch
import xyz.jdynb.music.R
import xyz.jdynb.music.base.BaseMusicNavFragment
import xyz.jdynb.music.config.Api
import xyz.jdynb.music.databinding.FragmentSearchMusicListBinding
import xyz.jdynb.music.event.Searchable
import xyz.jdynb.music.model.MusicModel
import xyz.jdynb.music.model.Page
import xyz.jdynb.music.model.setupMusicRv

/**
 * 搜索音乐列表
 */
class SearchMusicListFragment :
        BaseSearchListFragment<FragmentSearchMusicListBinding>(R.layout.fragment_search_music_list){

  override fun getMusicModels(): List<Any?>? {
    return binding.rvMusic.models
  }

  override fun initView() {
    binding.page.onRefresh {
      scope {
        val result = Get<Page<MusicModel>>(Api.SEARCH) {
          addQuery("keyword", keyword)
          addQuery("pageNo", index)
          addQuery("pageSize", 20)
        }.await()
        addData(result.data.onEach {
          mainViewModel.getMusicModelState(it)
        }) {
          result.total > modelCount
        }
        result.page = index
        addPage(result)
      }
    }

    binding.rvMusic.setupMusicRv(this)

    if (mData.isNotEmpty()) {
      binding.page.index = pageNo
      binding.rvMusic.models = mData.toMutableList()
    }
  }

  override fun onSearch() {
    binding.page.showLoading()
  }
}

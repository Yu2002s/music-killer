package xyz.jdynb.music.ui.fragment.favorite

import com.drake.net.utils.withIO
import org.litepal.LitePal
import org.litepal.extension.findAll
import xyz.jdynb.music.R
import xyz.jdynb.music.base.BaseMusicAppbarFragment
import xyz.jdynb.music.databinding.FragmentFavoriteBinding
import xyz.jdynb.music.model.FavoriteModel
import xyz.jdynb.music.model.MusicModel
import xyz.jdynb.music.model.setupMusicRv
import xyz.jdynb.music.utils.onLoad

class FavoriteFragment :
  BaseMusicAppbarFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

  override fun openMediaController(): Boolean {
    return true
  }

  override fun isAddScrollView(): Boolean {
    return false
  }

  override fun initView() {
    binding.rvFavorite.setupMusicRv(this)

    binding.page.onLoad(this) { page ->
      withIO {
        LitePal.findAll<FavoriteModel>().map {
          MusicModel(
            name = it.name,
            pic = it.cover,
            hasLossless = it.hasLossless,
            id = it.musicId,
            artist = it.author,
            artistId = it.authorId,
            duration = it.duration
          )
        }
      }.also {
        page.addData(it)
      }
    }
  }

  override fun initData() {

  }
}
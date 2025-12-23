package xyz.jdynb.music.ui.activity

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import com.drake.net.utils.scope
import com.drake.net.utils.withIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findFirst
import xyz.jdynb.music.MusicKillerApplication
import xyz.jdynb.music.constants.IntentActions
import xyz.jdynb.music.constants.IntentExtras
import xyz.jdynb.music.model.FavoriteModel
import xyz.jdynb.music.model.MusicModel
import xyz.jdynb.music.model.PlayHistory

class MainViewModel : ViewModel() {

  private val _bottomBarUIState = MutableStateFlow(BottomBarState())
  val bottomBarUIState: StateFlow<BottomBarState> = _bottomBarUIState.asStateFlow()

  private val _musicModel =
    MutableStateFlow(MusicModel(pic = "", name = "请选择音乐播放", artist = "MusicKiller"))
  val musicModel = _musicModel.asStateFlow()

  private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_ALL)
  val repeatMode = _repeatMode.asStateFlow()

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying get() = _isPlaying.asStateFlow()

  private val _currentPosition = MutableStateFlow(0L)
  val currentPosition = _currentPosition.asStateFlow()

  fun changeBottomBarVisible(isVisible: Boolean = true) {
    _bottomBarUIState.update {
      BottomBarState(isVisible = isVisible, isExpanded = it.isExpanded)
    }
  }

  fun changeBottomBarExpand(isExpanded: Boolean = false) {
    _bottomBarUIState.update {
      BottomBarState(isVisible = it.isVisible, isExpanded = isExpanded)
    }
  }

  suspend fun updateMusicModel(musicModel: MusicModel) {
    musicModel.isFavorite = getMusicFavorite(musicModel) != -1L
    _musicModel.value = musicModel

    withIO {
      // 加入到播放历史中
      PlayHistory.from(musicModel).saveOrUpdate("musicId = ?", musicModel.id.toString())
    }
  }

  fun updateRepeatMode(@Player.RepeatMode mode: Int) {
    _repeatMode.value = mode
  }

  fun updateIsPlaying(isPlaying: Boolean = !_isPlaying.value) {
    _isPlaying.value = isPlaying
  }

  fun updateCurrentPosition(position: Long) {
    _currentPosition.value = position
  }

  suspend fun getMusicModelState(model: MusicModel) {
    model.isSelected = _musicModel.value.id == model.id
    model.isFavorite = isFavoriteMusic(model)
  }

  suspend fun getMusicFavorite(musicModel: MusicModel) = withIO {
    if (musicModel.id == 0L) return@withIO -1
    val favoriteModel = LitePal.where("${IntentExtras.MUSIC_ID} = ?", musicModel.id.toString())
      .findFirst<FavoriteModel>()
    favoriteModel?.id ?: -1
  }

  suspend fun isFavoriteMusic(musicModel: MusicModel): Boolean {
    return getMusicFavorite(musicModel) != -1L
  }

  fun addOrRemoveFavorite(model: MusicModel = musicModel.value) {
    if (model.id <= 0L) {
      return
    }
    scope(Dispatchers.IO) {
      if (!isFavoriteMusic(model)) {
        model.isFavorite = true
        FavoriteModel(model).save()
      } else {
        model.isFavorite = false
        LitePal.deleteAll<FavoriteModel>("${IntentExtras.MUSIC_ID} = ?", model.id.toString())
      }
      MusicKillerApplication.context.sendBroadcast(
        Intent(IntentActions.FAVORITE)
          .setPackage(MusicKillerApplication.context.packageName)
          .putExtra(IntentExtras.MUSIC_ID, model.id)
          .putExtra(IntentExtras.FAVORITE, model.isFavorite)
      )
    }
  }

  data class BottomBarState(
    val isExpanded: Boolean = false,
    val isVisible: Boolean = true,
  )
}
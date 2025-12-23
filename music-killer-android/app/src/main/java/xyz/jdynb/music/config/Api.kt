package xyz.jdynb.music.config

object Api {
  val BASE_API: String = if (false/*BuildConfig.DEBUG*/) {
    "http://192.168.31.139"
    // BASE_API = "http://192.168.1.42"
  } else {
    "http://119.3.221.4:8002"
  }
  // const val BASE_API = "http://192.168.1.42"

  const val HOME_DATA = "/home/data"

  /**
   * 歌单标签列表
   */
  const val PLAYLIST_INDEX_TAGS = "/playlist/getIndexPlayListTags"

  /**
   * 获取推荐歌单列表
   */
  const val PLAYLIST_RECOMMEND = "/playlist/recommend"

  /**
   * 通过标签获取歌单列表
   */
  const val PLAYLIST_BY_TAG = "/playlist/getPlayListByTag"

  /**
   * 获取首页的排行榜数据
   */
  const val RANK_INDEX = "/rank/index"

  /**
   * 排行榜菜单
   */
  const val RANK_MENU = "/rank/menu"

  /**
   * 排行榜音乐列表
   */
  const val RANK_MUSIC_LIST = "/rank/getMusicList"

  /**
   * 获取歌单标签列表
   */
  const val PLAYLIST_TAGS = "/playlist/getPlayListTags"

  /**
   * 获取歌单分页列表
   */
  const val PLAYLIST_PAGE = "/playlist/page"

  /**
   * 获取标签的歌单列表
   */
  const val TAG_PLAYLIST_PAGE = "/playlist/getTagPlaylist"

  /**
   * 歌单信息
   */
  const val PLAYLIST_INFO = "/playlist/info"

  /**
   * 获取播放信息
   */
  const val PLAY_INFO = "/play/info"

  /**
   * 歌手列表
   */
  const val ARTIST_LIST = "/artist/list"

  const val ARTIST_MUSIC = "/artist/music"

  const val ARTIST_INFO = "/artist/info"

  /**
   * 搜索建议
   */
  const val SEARCH_KEYWORD = "/search/key"

  /**
   * 搜索歌曲
   */
  const val SEARCH = "/search"

  const val SEARCH_ALBUM = "/search/album"

  const val SEARCH_PLAYLIST = "/search/playlist"

  const val SEARCH_ARTIST = "/search/artist"

}
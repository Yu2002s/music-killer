package xyz.jdynb.music.model

data class UpdateModel(
  val versionName: String = "",
  val versionCode: Long = 0,
  val url: String = "",
  val updateTime: String = "",
)

package leo8

sealed class Image

data class UrlImage(val url: Url) : Image()

val Url.image: Image get() = UrlImage(this)

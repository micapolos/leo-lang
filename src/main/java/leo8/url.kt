package leo8

data class Url(val string: String)

val String.url get() = Url(this)
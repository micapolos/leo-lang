package leo7

data class Quoted<out T>(val unquoted: T)

val <T> T.quoted get() = Quoted(this)

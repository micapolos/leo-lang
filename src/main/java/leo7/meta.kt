package leo7

data class Meta<out T>(val unmeta: T)

val <T> T.meta get() = Meta(this)

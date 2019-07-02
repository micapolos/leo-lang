package leo7.frp

data class BoolSelector(val bool: Bool)

val Bool.selector get() = BoolSelector(this)

package leo7.frp

data class Animation(val start: NumberStart, val speed: Speed)

fun animation(start: NumberStart, speed: Speed) = Animation(start, speed)

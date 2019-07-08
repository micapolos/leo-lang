package leo8

data class AnimationFrame(val animation: Animation)

val Animation.frame get() = AnimationFrame(this)

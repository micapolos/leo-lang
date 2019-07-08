package leo8

sealed class Event

data class KeyPressEvent(val keyPress: KeyPress) : Event()
data class AnimationFrameEvent(val animationFrame: AnimationFrame) : Event()

val KeyPress.event: Event get() = KeyPressEvent(this)
val AnimationFrame.event: Event get() = AnimationFrameEvent(this)

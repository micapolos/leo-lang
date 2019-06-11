package leo5.asm

data class Input(val fn: () -> Int)

fun input(fn: () -> Int) = Input(fn)
val Input.int get() = fn()

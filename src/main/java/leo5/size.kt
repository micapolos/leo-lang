package leo5

data class Size(val int: Int)

fun size(int: Int) = Size(int)
val Size.decrement get() = size(int.dec())

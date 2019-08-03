package leo13

data class IntArgument(val int: Int)

val Int.argument get() = IntArgument(this)
fun argument(int: Int) = int.argument

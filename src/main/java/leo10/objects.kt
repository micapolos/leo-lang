package leo10

object Lhs
object Rhs
object Last
object Previous
object Begin
object End

val lhs = Lhs
val rhs = Rhs
val last = Last
val previous = Previous
val begin = Begin
val end = End

data class Head<out T>(val value: T)
data class Tail<out T>(val value: T)

fun <T> head(value: T) = Head(value)
fun <T> tail(value: T) = Tail(value)

fun <T> Head<T>.invoke() = value
fun <T> Tail<T>.invoke() = value

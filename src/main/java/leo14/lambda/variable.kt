package leo14.lambda

import leo.binary.zero
import leo13.Index
import leo13.index
import leo13.int
import leo13.next

data class Variable<out T>(val index: Index) {
	override fun toString() = "v${-index.int}"
}

fun <T> variable(index: Index) = Variable<T>(index)
fun <T> variable() = variable<T>(zero.index)
val <T> Variable<T>.previous get() = variable<T>(index.next)

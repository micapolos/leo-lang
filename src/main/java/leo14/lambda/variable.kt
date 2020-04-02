package leo14.lambda

import leo13.Index

data class Variable<out T>(val index: Index) {
	override fun toString() = "${index}"
}

fun <T> variable(index: Index) = Variable<T>(index)
fun <T> variable() = variable<T>(0)
val <T> Variable<T>.previous get() = variable<T>(index.inc())

package leo14.lambda.runtime.typed

import leo.base.println
import leo14.lambda.runtime.Value

val int = "int"
val string = "string"

data class ListT(val type: Type) {
	override fun toString() = "list($type)"
}

fun typed(i: Int) = typed(int) { i }
fun typed(s: String) = typed(string) { s }
fun typedList(type: Value, vararg items: Typed) = listOf(*items).map { it.check(type) }.let { list ->
	typed(ListT(type)) { list }
}

fun id(type: Type) = fn(type) { it }

fun first(t1: Type, t2: Type) = fn(t1) { v1 -> fn(t2) { v2 -> v1 } }
fun second(t1: Type, t2: Type) = fn(t1) { v1 -> fn(t2) { v2 -> v2 } }

fun intOp(value: Value) = typed(int to int) { value }
fun intOp2(value: Value) = typed(int to (int to int)) { value }

val intNegate = intOp(leo14.lambda.runtime.intNegate)
val intPlusInt = intOp2(leo14.lambda.runtime.intPlusInt)
val intMinusInt = intOp2(leo14.lambda.runtime.intMinusInt)
val intTimesInt = intOp2(leo14.lambda.runtime.intTimesInt)
val intString = typed(int to string) { leo14.lambda.runtime.intString }

val stringLength = typed(string to int) { leo14.lambda.runtime.stringLength }
val stringPlusString = typed(string to (string to string)) { leo14.lambda.runtime.stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListT(from) to ((from to to) to ListT(to))) { leo14.lambda.runtime.listMap }

fun Typed.dot(fn: Typed): Typed = fn(this)

fun main() =
	typed("Magic number: ")
		.dot(stringPlusString)(
		typed("Hello, ")
			.dot(stringPlusString)(typed("world!"))
			.dot(stringLength)
			.dot(intTimesInt)(typed(10000))
			.dot(intString))
		.value
		.println

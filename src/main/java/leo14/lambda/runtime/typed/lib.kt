package leo14.lambda.runtime.typed

import leo.base.println
import leo14.lambda.runtime.Value

object Text {
	override fun toString() = "Text"
}

object I32 {
	override fun toString() = "I32"
}

data class ListOf(val type: Type) {
	override fun toString() = "list($type)"
}

fun typed(i: Int) = typed(I32) { i }
fun typed(s: String) = typed(Text) { s }
fun typedList(type: Value, vararg items: Typed) = listOf(*items).map { it.check(type) }.let { list ->
	typed(ListOf(type)) { list }
}

fun intOp(value: Value) = typed(I32 to I32) { value }
fun intOp2(value: Value) = typed(I32 to (I32 to I32)) { value }

val intNegate = intOp(leo14.lambda.runtime.intNegate)
val intPlusInt = intOp2(leo14.lambda.runtime.intPlusInt)
val intMinusInt = intOp2(leo14.lambda.runtime.intMinusInt)
val intTimesInt = intOp2(leo14.lambda.runtime.intTimesInt)
val intString = typed(I32 to Text) { leo14.lambda.runtime.intString }

val stringLength = typed(Text to I32) { leo14.lambda.runtime.stringLength }
val stringPlusString = typed(Text to (Text to Text)) { leo14.lambda.runtime.stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListOf(from) to ((from to to) to ListOf(to))) { leo14.lambda.runtime.listMap }

fun Typed.apply(fn: Typed): Typed = fn(this)
fun Typed.apply(fn: Typed, typed: Typed): Typed = apply(fn)(typed)

fun main() =
	typed("Magic number: ")
		.apply(
			stringPlusString,
			typed("Hello, ")
				.apply(stringPlusString, typed("world!"))
				.apply(stringLength)
				.apply(intTimesInt, typed(10000))
				.apply(intString))
		.value
		.println

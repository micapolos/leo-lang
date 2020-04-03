package leo14.lambda.runtime.typed

import leo14.lambda.runtime.Value
import leo14.lambda.runtime.intMinusInt
import leo14.lambda.runtime.intNegate
import leo14.lambda.runtime.intPlusInt
import leo14.lambda.runtime.intString
import leo14.lambda.runtime.intTimesInt
import leo14.lambda.runtime.listMap
import leo14.lambda.runtime.stringLength
import leo14.lambda.runtime.stringPlusString

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
fun intOp2(value: Value) = typed(I32 to (I32 to Text)) { value }

val intNegate = intOp(intNegate)
val intPlusInt = intOp2(intPlusInt)
val intMinusInt = intOp2(intMinusInt)
val intTimesInt = intOp2(intTimesInt)
val intString = typed(I32 to Text) { intString }

val stringLength = typed(Text to I32) { stringLength }
val stringPlusString = typed(Text to (Text to Text)) { stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListOf(from) to ((from to to) to ListOf(to))) { listMap }

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

object string {
	override fun toString() = "string"
}

object int {
	override fun toString() = "int"
}

data class ListOf(val value: Value)

fun typed(i: Int) = typed(int) { i }
fun typed(s: String) = typed(string) { s }
fun typedList(type: Value, vararg items: Typed) = listOf(*items).map { it.check(type) }.let { list ->
	typed(ListOf(type)) { list }
}

fun intOp(value: Value) = typed(int to int) { value }
fun intOp2(value: Value) = typed(int to (int to string)) { value }

val intNegate = intOp(intNegate)
val intPlusInt = intOp2(intPlusInt)
val intMinusInt = intOp2(intMinusInt)
val intTimesInt = intOp2(intTimesInt)
val intString = typed(int to string) { intString }

val stringLength = typed(string to int) { stringLength }
val stringPlusString = typed(string to (string to string)) { stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListOf(from) to ((from to to) to ListOf(to))) { listMap }

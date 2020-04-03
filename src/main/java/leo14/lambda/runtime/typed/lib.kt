package leo14.lambda.runtime.typed

import leo14.lambda.runtime.Value

data class ListOf(val value: Value)

val string = String::class
val int = Int::class

fun typed(int: Int) = typed(Int::class) { int }
fun typed(string: String) = typed(String::class) { string }
fun typedList(type: Value, vararg items: Typed) = listOf(*items).map { it.check(type) }.let { list ->
	typed(ListOf(type)) { list }
}

fun intOp(value: Value) = typed(int to int) { value }
fun intOp2(value: Value) = typed(int to (int to string)) { value }

val intNegate = intOp2(leo14.lambda.runtime.intNegate)
val intPlusInt = intOp2(leo14.lambda.runtime.intPlusInt)
val intMinusInt = intOp2(leo14.lambda.runtime.intMinusInt)
val intTimesInt = intOp2(leo14.lambda.runtime.intTimesInt)
val intString = typed(int to string) { leo14.lambda.runtime.intString }

val stringLength = typed(string to int) { leo14.lambda.runtime.stringLength }
val stringPlusString = typed(string to (string to string)) { leo14.lambda.runtime.stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListOf(from) to ((from to to) to ListOf(to))) { leo14.lambda.runtime.listMap }

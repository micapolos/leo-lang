package leo14.lambda.runtime.typed

import leo.base.println
import leo14.lambda.runtime.*

val nil = "nil"
val int = "int"
val double = "double"
val string = "string"

data class ListT(val type: Type) {
	override fun toString() = "list($type)"
}

data class Pair(val type: Type, val make: Typed, val first: Typed, val second: Typed)
data class Either(val type: Type, val makeFirst: Typed, val makeSecond: Typed, val switch: (Type) -> Typed)
data class Alias(val type: Type, val make: Typed, val get: Typed)

class TypeId(val string: String) {
	override fun toString() = string
}

val typedNil = typed(nil) { null }
fun typed(i: Int) = typed(int) { i }
fun typed(d: Double) = typed(double) { d }
fun typed(s: String) = typed(string) { s }
fun typedList(type: Value, vararg items: Typed) = listOf(*items).map { it.check(type) }.let { list ->
	typed(ListT(type)) { list }
}

fun id(type: Type) = fn(type) { it }

fun first(t1: Type, t2: Type) = fn(t1) { v1 -> fn(t2) { v2 -> v1 } }
fun second(t1: Type, t2: Type) = fn(t1) { v1 -> fn(t2) { v2 -> v2 } }

fun pair(name: String, t1: Type, t2: Type) =
	TypeId(name).let { type ->
		Pair(
			type = type,
			make = typed(t1 to (t2 to type)) { pair },
			first = typed(type to t1) {
				valueFn { pair ->
					pair.valueInvoke(first)
				}
			},
			second = typed(type to t2) {
				valueFn { pair ->
					pair.valueInvoke(second)
				}
			})
	}

fun either(name: String, t1: Type, t2: Type) =
	TypeId(name).let { type ->
		Either(
			type = type,
			makeFirst = typed(t1 to type) { firstOfTwo },
			makeSecond = typed(t2 to type) { secondOfTwo },
			switch = { result ->
				typed(type to ((t1 to result) to ((t2 to result) to result))) { id }
			}
		)
	}

fun alias(name: String, aliased: Type) =
	TypeId(name).let { type ->
		Alias(
			type,
			typed(aliased to type) { id },
			typed(type to aliased) { id })
	}

fun intOp(value: Value) = typed(int to int) { value }
fun intOp2(value: Value) = typed(int to (int to int)) { value }

val intNegate = intOp(leo14.lambda.runtime.intNegate)
val intPlusInt = intOp2(leo14.lambda.runtime.intPlusInt)
val intMinusInt = intOp2(leo14.lambda.runtime.intMinusInt)
val intTimesInt = intOp2(leo14.lambda.runtime.intTimesInt)
val intString = typed(int to string) { leo14.lambda.runtime.intString }

val doubleString = typed(double to string) { leo14.lambda.runtime.doubleString }

val stringLength = typed(string to int) { leo14.lambda.runtime.stringLength }
val stringPlusString = typed(string to (string to string)) { leo14.lambda.runtime.stringPlusString }

fun listMap(from: Value, to: Value) = typed(ListT(from) to ((from to to) to ListT(to))) { listMap }

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

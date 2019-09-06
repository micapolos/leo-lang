package leo13.script.reflect

import leo13.fail
import leo13.script.Script
import leo13.script.script
import leo9.*

data class Struct<V : Any>(val fieldStack: Stack<Field<V, *>>, val valueFn: Stack<Any?>.() -> V)

fun <V : Any> struct(value: V): Struct<V> =
	Struct(stack()) { value }

fun <V : Any, V1 : Any> struct(
	firstField: Field<V, V1>,
	valueFn: (V1) -> V) =
	Struct(stack(firstField)) {
		valueFn(unsafeGet(0) as V1)
	}

fun <V : Any, V1 : Any, V2 : Any> struct(
	firstField: Field<V, V1>,
	secondField: Field<V, V2>,
	valueFn: (V1, V2) -> V) =
	Struct(stack(firstField, secondField)) {
		valueFn(unsafeGet(1) as V1, unsafeGet(0) as V2)
	}

fun <V : Any> Struct<V>.script(value: V): Script =
	fieldStack.map { scriptLine(value) }.script

fun <V : Any> Struct<V>.unsafeValue(script: Script): V =
	zipMapOrNull(fieldStack, script.lineStack) { field, line ->
		field.unsafeValue(line)
	}
		?.valueFn()
		?: fail("error")

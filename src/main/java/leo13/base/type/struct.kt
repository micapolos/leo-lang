package leo13.base.type

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

fun <V : Any, V1 : Any, V2 : Any, V3 : Any, V4 : Any, V5 : Any, V6 : Any, V7 : Any, V8 : Any> struct(
	field1: Field<V, V1>,
	field2: Field<V, V2>,
	field3: Field<V, V3>,
	field4: Field<V, V4>,
	field5: Field<V, V5>,
	field6: Field<V, V6>,
	field7: Field<V, V7>,
	field8: Field<V, V8>,
	valueFn: (V1, V2, V3, V4, V5, V6, V7, V8) -> V) =
	Struct(stack(field1, field2, field3, field4, field5, field6, field7, field8)) {
		valueFn(
			unsafeGet(7) as V1,
			unsafeGet(6) as V2,
			unsafeGet(5) as V3,
			unsafeGet(4) as V4,
			unsafeGet(3) as V5,
			unsafeGet(2) as V6,
			unsafeGet(1) as V7,
			unsafeGet(0) as V8)
	}

fun <V : Any> Struct<V>.script(value: V): Script =
	fieldStack.map { scriptLine(value) }.script

fun <V : Any> Struct<V>.unsafeValue(script: Script): V =
	zipMapOrNull(fieldStack, script.lineStack) { field, line ->
		field.unsafeValue(line)
	}
		?.valueFn()
		?: fail("error")

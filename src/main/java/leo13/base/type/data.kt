package leo13.base.type

import leo13.fail
import leo13.script.lineTo
import leo9.unsafeGet

fun type(name: String): Type<Unit> =
	type(name, Unit)

fun <V : Any> type(name: String, value: V): Type<V> =
	Type(
		name,
		{ leo13.script.script(name) },
		{ if (this == leo13.script.script(name)) value else fail("expected" lineTo leo13.script.script(name)) })

fun <V : Any, V1 : Any> type(name: String, field1: Field<V, V1>, newFn: (V1) -> V): Type<V> =
	Type(
		name,
		{ leo13.script.script(field1.type.scriptLine(field1.getFn(this))) },
		{ newFn(field1.unsafeValue(lineStack.unsafeGet(0))) })

fun <V : Any, V1 : Any, V2 : Any> type(
	name: String,
	field1: Field<V, V1>,
	field2: Field<V, V2>,
	newFn: (V1, V2) -> V): Type<V> =
	Type(
		name,
		{
			leo13.script.script(
				field1.type.scriptLine(field1.getFn(this)),
				field2.type.scriptLine(field2.getFn(this)))
		},
		{
			newFn(
				field1.unsafeValue(lineStack.unsafeGet(1)),
				field2.unsafeValue(lineStack.unsafeGet(0)))
		})

fun <V : Any, V1 : Any, V2 : Any, V3 : Any, V4 : Any, V5 : Any, V6 : Any, V7 : Any, V8 : Any> type(
	name: String,
	field1: Field<V, V1>,
	field2: Field<V, V2>,
	field3: Field<V, V3>,
	field4: Field<V, V4>,
	field5: Field<V, V5>,
	field6: Field<V, V6>,
	field7: Field<V, V7>,
	field8: Field<V, V8>,
	newFn: (V1, V2, V3, V4, V5, V6, V7, V8) -> V): Type<V> =
	Type(
		name,
		{
			leo13.script.script(
				field1.type.scriptLine(field1.getFn(this)),
				field2.type.scriptLine(field2.getFn(this)),
				field3.type.scriptLine(field3.getFn(this)),
				field4.type.scriptLine(field4.getFn(this)),
				field5.type.scriptLine(field5.getFn(this)),
				field6.type.scriptLine(field6.getFn(this)),
				field7.type.scriptLine(field7.getFn(this)),
				field8.type.scriptLine(field8.getFn(this)))
		},
		{
			newFn(
				field1.unsafeValue(lineStack.unsafeGet(7)),
				field2.unsafeValue(lineStack.unsafeGet(6)),
				field3.unsafeValue(lineStack.unsafeGet(5)),
				field4.unsafeValue(lineStack.unsafeGet(4)),
				field5.unsafeValue(lineStack.unsafeGet(3)),
				field6.unsafeValue(lineStack.unsafeGet(2)),
				field7.unsafeValue(lineStack.unsafeGet(1)),
				field8.unsafeValue(lineStack.unsafeGet(0)))
		})

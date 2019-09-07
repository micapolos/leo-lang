package leo13.base.type

import leo13.fail
import leo13.script.*
import leo9.Stack
import leo9.unsafeGet

data class Type<out V : Any>(val name: String, val body: Body<V>)

data class Type2<V : Any>(
	val name: String,
	val bodyScript: V.() -> Script,
	val unsafeBodyValue: Script.() -> V)

fun <V : Any> Type2<V>.scriptLine(value: V) = name lineTo bodyScript(value)

fun <V : Any> type(name: String, body: Body<V>) = Type(name, body)

fun <V : Any> optionType(orNullType: Type<V>): Type<leo13.base.Option<V>> =
	type(orNullType.name, body(option(orNullType)))

fun <V : Any> listType(itemType: Type<V>): Type<Stack<V>> =
	type(itemType.name, body(list(itemType)))

fun <V : Any> aliasType(name: String, type: Type<V>): Type<V> =
	type(name, body(struct(field(type) { this }) { it }))

fun <V : Any, A : Any> nativeType(type: Type<A>, getFn: V.() -> A, newFn: A.() -> V): Type<V> =
	type(type.name, body(native(type, getFn, newFn)))

fun <V : Any> Type<V>.scriptLine(value: V): ScriptLine =
	name lineTo bodyScript(value)

fun <V : Any> Type<V>.bodyScript(value: V): Script =
	body.script(value)

fun <V : Any> Type<V>.script(value: V): Script =
	script(scriptLine(value))

fun <V : Any> Type<V>.unsafeValue(scriptLine: ScriptLine): V =
	if (name != scriptLine.name) fail("expected" lineTo script(name))
	else unsafeBodyValue(scriptLine.rhs)

fun <V : Any> Type<V>.unsafeBodyValue(script: Script): V =
	body.unsafeValue(script)

fun <V : Any> Type<V>.unsafeValue(script: Script): V =
	unsafeValue(script.unsafeOnlyLine)

fun <V : Any> Type<V>.toString(value: V): String =
	scriptLine(value).toString()

fun type(name: String): Type2<Unit> =
	type(name, Unit)

fun <V : Any> type(name: String, value: V): Type2<V> =
	Type2(
		name,
		{ script(name) },
		{ if (this == script(name)) value else fail("expected" lineTo script(name)) })

fun <V : Any, V1 : Any> type(name: String, field1: Field<V, V1>, newFn: (V1) -> V): Type2<V> =
	Type2(
		name,
		{ script(field1.type.scriptLine(field1.getFn(this))) },
		{ newFn(field1.unsafeValue(lineStack.unsafeGet(0))) })

fun <V : Any, V1 : Any, V2 : Any> type(
	name: String,
	field1: Field<V, V1>,
	field2: Field<V, V2>,
	newFn: (V1, V2) -> V): Type2<V> =
	Type2(
		name,
		{
			script(
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
	newFn: (V1, V2, V3, V4, V5, V6, V7, V8) -> V): Type2<V> =
	Type2(
		name,
		{
			script(
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

fun <V : Any, V1 : Any, V2 : Any> type(
	name: String,
	case1: Case<V, V1>,
	case2: Case<V, V2>,
	get: V.() -> Pair<Type<*>, Any>): Type2<V> =
	Type2(
		name,
		{ get().let { pair -> pair.first.bodyScript(pair.second) } },
		{
			unsafeOnlyLine
				.let { line ->
					when {
						case2.type.name == line.name -> case2.valueFn(case2.type.unsafeBodyValue(this))
						case1.type.name == line.name -> case1.valueFn(case1.type.unsafeBodyValue(this))
						else -> fail("mismatch" lineTo this)
					}
				}
		})

package leo13.base.type

import leo13.script.*
import leo9.Stack
import leo9.map

data class TypeList<V : Any>(val orNullType: Type<V>)

fun <V : Any> list(orNullType: Type<V>) = TypeList(orNullType)

fun <V : Any> TypeList<V>.unsafeBodyValue(script: Script): Stack<V> =
	script
		.unsafeOnlyLine
		.unsafeRhs("list")
		.lineStack.map { orNullType.unsafeValue(this) }

fun <V : Any> TypeList<V>.bodyScript(stack: Stack<V>): Script =
	script("list" lineTo stack.map { orNullType.scriptLine(this) }.script)

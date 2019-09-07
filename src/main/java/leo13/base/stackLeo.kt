package leo13.base

import leo13.base.type.Type
import leo13.base.type.scriptLine
import leo13.base.type.toString
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine
import leo13.script.unsafeRhs
import leo9.Stack
import leo9.map

fun <V : Any> stackType(itemType: Type<V>): Type<Stack<V>> =
	Type(
		itemType.name,
		{ script("list" lineTo map { itemType.scriptLine(this) }.script) },
		{ unsafeOnlyLine.unsafeRhs("list").lineStack.map { itemType.unsafeValue(this) } })

data class StackLeo<V : Any>(val itemType: Type<V>, val stack: Stack<V>) {
	override fun toString() = stackType(itemType).toString(stack)
}

fun <V : Any> leo(itemType: Type<V>, stack: Stack<V>) = StackLeo(itemType, stack)

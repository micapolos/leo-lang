package leo13.base

import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine
import leo13.script.unsafeRhs
import leo13.scripter.Scripter
import leo13.scripter.scriptLine
import leo13.scripter.toString
import leo13.scripter.unsafeValue
import leo9.Stack
import leo9.map

const val stackTypeName = "list"

fun <V : Any> stackType(itemType: Scripter<V>): Scripter<Stack<V>> =
	Scripter(
		itemType.name,
		{ script(stackTypeName lineTo map { itemType.scriptLine(this) }.script) },
		{ unsafeOnlyLine.unsafeRhs(stackTypeName).lineStack.map { itemType.unsafeValue(this) } })

data class StackLeo<V : Any>(val itemType: Scripter<V>, val stack: Stack<V>) {
	override fun toString() = stackType(itemType).toString(stack)
}

fun <V : Any> leo(itemType: Scripter<V>, stack: Stack<V>) = StackLeo(itemType, stack)

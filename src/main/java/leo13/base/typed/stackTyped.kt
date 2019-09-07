package leo13.base.typed

import leo13.base.Typed
import leo13.base.type.Type
import leo13.base.type.scriptLine
import leo13.base.type.type
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack
import leo9.map

fun <V : Any> stackType(itemType: Type<V>): Type<Stack<V>> =
	Type(
		itemType.name,
		{ script("list" lineTo map { itemType.scriptLine(this) }.script) },
		{ lineStack.map { itemType.unsafeValue(this) } })

data class StackTyped<V : Any>(val itemType: Type<V>, val stack: Stack<V>) : Typed<StackTyped<V>>() {
	override fun toString() = super.toString()
	override val type: Type<StackTyped<V>>
		get() =
			type(stackType(itemType), { stack }, { typed(itemType, this) })
}

fun <V : Any> typed(itemType: Type<V>, stack: Stack<V>) = StackTyped(itemType, stack)

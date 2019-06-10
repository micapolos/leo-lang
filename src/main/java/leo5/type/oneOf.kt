package leo5.type

import leo.base.Stack
import leo.base.indexed
import leo.base.orIfNull
import leo.base.stack
import leo5.Value

data class OneOf(val typeStack: Stack<Type>)

fun oneOf(typeStack: Stack<Type>) = OneOf(typeStack)
fun oneOf(type: Type, vararg types: Type) = oneOf(stack(type, *types))

fun OneOf.compile(value: Value): Any =
	compilePair(value).run { index indexed this.value!! }

fun OneOf.compilePair(value: Value): IndexedValue<Any?> =
	typeStack
		.tail
		?.let { oneOf(it).compilePair(value) }
		.orIfNull { 0 indexed null }
		.run {
			if (this.value == null)
				try {
					index indexed typeStack.head.compile(value)
				} catch (e: RuntimeException) {
					index.inc() indexed null
				}
			else this
		}

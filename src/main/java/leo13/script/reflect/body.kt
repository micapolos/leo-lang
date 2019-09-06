package leo13.script.reflect

import leo13.script.Script

sealed class Body<V : Any>
data class StructBody<V : Any>(val struct: Struct<V>) : Body<V>()
data class ChoiceBody<V : Any>(val choice: Choice<V>) : Body<V>()

fun <V : Any> body(struct: Struct<V>): Body<V> = StructBody(struct)
fun <V : Any> body(choice: Choice<V>): Body<V> = ChoiceBody(choice)

fun <V : Any> Body<V>.script(value: V): Script =
	when (this) {
		is StructBody -> struct.script(value)
		is ChoiceBody -> choice.script(value)
	}

fun <V : Any> Body<V>.unsafeValue(script: Script): V =
	when (this) {
		is StructBody -> struct.unsafeValue(script)
		is ChoiceBody -> choice.unsafeValue(script)
	}

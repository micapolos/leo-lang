package leo13.base.type

import leo13.script.Script
import leo9.Stack
import leo13.base.Option as ValueOption

sealed class Body<out V : Any>
data class StructBody<V : Any>(val struct: Struct<V>) : Body<V>()
data class ChoiceBody<V : Any>(val choice: Choice<V>) : Body<V>()
data class OptionBody<V : Any>(val option: TypeOption<V>) : Body<ValueOption<V>>()
data class ListBody<V : Any>(val list: TypeList<V>) : Body<Stack<V>>()

fun <V : Any> body(struct: Struct<V>): Body<V> = StructBody(struct)
fun <V : Any> body(choice: Choice<V>): Body<V> = ChoiceBody(choice)
fun <V : Any> body(option: TypeOption<V>): Body<ValueOption<V>> = OptionBody(option)
fun <V : Any> body(list: TypeList<V>): Body<Stack<V>> = ListBody(list)

fun <V : Any> Body<V>.script(value: V): Script =
	when (this) {
		is StructBody -> struct.script(value)
		is ChoiceBody -> choice.script(value)
		is OptionBody<*> -> (option as TypeOption<Any>).bodyScript(value as ValueOption<Any>)
		is ListBody<*> -> (list as TypeList<Any>).bodyScript(value as Stack<Any>)
	}

fun <V : Any> Body<V>.unsafeValue(script: Script): V =
	when (this) {
		is StructBody -> struct.unsafeValue(script)
		is ChoiceBody -> choice.unsafeValue(script)
		is OptionBody<*> -> (option as TypeOption<Any>).unsafeBodyValue(script) as V
		is ListBody<*> -> (list as TypeList<Any>).unsafeBodyValue(script) as V
	}

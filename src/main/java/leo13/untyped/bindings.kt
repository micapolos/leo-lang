package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script

data class Bindings(val list: List<Binding>) : LeoStruct("bindings", list) {
	override fun toString() = super.toString()
}

fun bindings(list: List<Binding>) = Bindings(list)
fun bindings(vararg bindings: Binding) = Bindings(list(*bindings))
fun Bindings.plus(binding: Binding) = Bindings(list.plus(binding))

fun Bindings.valueOrNull(script: Script): Value? =
	list.mapFirst { valueOrNull(script) }
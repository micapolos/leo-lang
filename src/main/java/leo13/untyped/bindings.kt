package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script

data class bindings(val list: List<binding> = list()) : LeoStruct("bindings", list) {
	override fun toString() = super.toString()
}

fun bindings(vararg bindings: binding) = bindings(list(*bindings))
fun bindings.plus(binding: binding) = bindings(list.plus(binding))

fun bindings.valueOrNull(script: Script): value? =
	list.mapFirst { valueOrNull(script) }
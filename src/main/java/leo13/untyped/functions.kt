package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script

data class Functions(val list: List<Function>) : LeoStruct("functions", list) {
	override fun toString() = super.toString()
}

fun functions(vararg functions: Function) = Functions(list(*functions))
fun Functions.plus(function: Function) = Functions(list.plus(function))

fun Functions.bodyOrNull(script: Script): Body? =
	list.mapFirst { bodyOrNull(script) }
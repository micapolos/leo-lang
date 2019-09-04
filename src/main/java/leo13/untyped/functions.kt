package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script

data class functions(val list: List<function> = list()) : LeoStruct("functions", list) {
	override fun toString() = super.toString()
}

fun functions(vararg functions: function) = functions(list(*functions))
fun functions.plus(function: function) = functions(list.plus(function))

fun functions.bodyOrNull(script: Script): body? =
	list.mapFirst { bodyOrNull(script) }
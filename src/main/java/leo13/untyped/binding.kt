package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.Script

data class binding(val key: key, val value: value) : LeoStruct("binding", key, value) {
	override fun toString() = super.toString()
}

fun binding.valueOrNull(script: Script): value? =
	notNullIf(key.script == script) { value }
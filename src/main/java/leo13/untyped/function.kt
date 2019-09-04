package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.Script

data class function(
	val pattern: pattern = pattern(),
	val body: body = body()) : LeoStruct("function", pattern, body) {
	override fun toString() = super.toString()
}

fun function.bodyOrNull(script: Script): body? =
	notNullIf(script.matches(pattern)) { body }
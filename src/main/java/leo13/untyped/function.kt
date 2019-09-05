package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.Script

data class Function(
	val pattern: Pattern,
	val body: Body) : LeoStruct("function", pattern, body) {
	override fun toString() = super.toString()
}

fun function(pattern: Pattern, body: Body) = Function(pattern, body)
fun Function.bodyOrNull(script: Script): Body? =
	notNullIf(script.matches(pattern)) { body }
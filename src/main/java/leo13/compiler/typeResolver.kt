package leo13.compiler

import leo.base.fold
import leo13.LeoObject
import leo13.script.*

data class TypeResolver(
	val typeFunctions: TypeFunctions,
	val resolvedScript: Script) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "type"
	override val scriptableBody
		get() = script(
			"resolver" lineTo script(
				typeFunctions.scriptableLine,
				"resolved" lineTo resolvedScript))
}

fun resolver(typeFunctions: TypeFunctions, resolvedScript: Script) =
	TypeResolver(typeFunctions, resolvedScript)

fun TypeResolver.push(script: Script): TypeResolver =
	fold(script.lineSeq) { push(it) }

fun TypeResolver.push(scriptLine: ScriptLine): TypeResolver =
	if (resolvedScript.isEmpty && scriptLine.rhs.isEmpty)
		append(typeFunctions.resolve(scriptLine.name).scriptableBody)
	else
		append(scriptLine.name lineTo typeFunctions.resolve(scriptLine.rhs))

fun TypeResolver.append(script: Script): TypeResolver =
	fold(script.lineSeq) { append(it) }

fun TypeResolver.append(scriptLine: ScriptLine): TypeResolver =
	resolver(typeFunctions, resolvedScript.plus(scriptLine))

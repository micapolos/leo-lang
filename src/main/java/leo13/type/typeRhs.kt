package leo13.type

import leo13.LeoObject
import leo13.script.Script
import leo13.script.lineTo

sealed class TypeRhs : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "rhs"
	override val scriptableBody get() = rhsScriptableBody // TODO: This is wrong, cause it does not support recursion
	abstract val rhsScriptableName: String
	abstract val rhsScriptableBody: Script
	val rhsScriptableLine get() = rhsScriptableName lineTo rhsScriptableBody
}

data class TypeTypeRhs(val type: Type) : TypeRhs() {
	override fun toString() = super.toString()
	override val rhsScriptableName get() = type.scriptableName
	override val rhsScriptableBody get() = type.scriptableBody
}

data class RecursionTypeRhs(val recursion: Recursion) : TypeRhs() {
	override fun toString() = super.toString()
	override val rhsScriptableName get() = recursion.scriptableName
	override val rhsScriptableBody get() = recursion.scriptableBody
}

fun rhs(type: Type): TypeRhs = TypeTypeRhs(type)
fun rhs(recursion: Recursion): TypeRhs = RecursionTypeRhs(recursion)

fun TypeRhs.contains(thunk: TypeRhs): Boolean =
	when (this) {
		is TypeTypeRhs -> thunk is TypeTypeRhs && type.contains(thunk.type)
		is RecursionTypeRhs -> thunk is RecursionTypeRhs && recursion == thunk.recursion
	}

val TypeRhs.unsafeStaticScript: Script
	get() =
		when (this) {
			is TypeTypeRhs -> type.unsafeStaticScript
			is RecursionTypeRhs -> TODO()
		}

val TypeRhs.typeOrNull: Type?
	get() =
		(this as? TypeTypeRhs)?.type
package leo13.type

import leo13.script.Script
import leo13.script.Scriptable

sealed class TypeThunk : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "thunk"
	override val scriptableBody get() = thunkScriptableBody
	abstract val thunkScriptableBody: Script
}

data class TypeTypeThunk(val type: Type) : TypeThunk() {
	override fun toString() = super.toString()
	override val thunkScriptableBody get() = type.scriptableBody
}

data class RecursionTypeThunk(val recursion: Recursion) : TypeThunk() {
	override fun toString() = super.toString()
	override val thunkScriptableBody get() = recursion.scriptableBody
}

fun thunk(type: Type): TypeThunk = TypeTypeThunk(type)
fun thunk(recursion: Recursion): TypeThunk = RecursionTypeThunk(recursion)

fun TypeThunk.contains(thunk: TypeThunk): Boolean =
	when (this) {
		is TypeTypeThunk -> thunk is TypeTypeThunk && type.contains(thunk.type)
		is RecursionTypeThunk -> thunk is RecursionTypeThunk && recursion == thunk.recursion
	}

val TypeThunk.unsafeStaticScript: Script
	get() =
		when (this) {
			is TypeTypeThunk -> type.unsafeStaticScript
			is RecursionTypeThunk -> TODO()
		}

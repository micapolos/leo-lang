package leo13.compiler

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.asScript
import leo13.type.Type
import leo13.type.contains
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Types(val typeStack: Stack<Type>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "traces"
	override val scriptableBody get() = typeStack.asScript { scriptableLine }
}

val Stack<Type>.types get() = Types(this)
fun types() = stack<Type>().types
fun types(vararg types: Type) = stack(*types).types
fun Types.plus(type: Type) = typeStack.push(type).types
fun Types.resolve(type: Type): Type =
	typeStack.mapFirst {
		notNullIf(contains(type)) {
			this
		}
	} ?: type

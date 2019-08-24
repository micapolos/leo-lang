package leo13

import leo9.*

data class TypeBindings(val stack: Stack<Type>) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = stack.asScriptLine("bindings") { asScriptLine }
}

val Stack<Type>.typeBindings get() = TypeBindings(this)
fun TypeBindings.push(type: Type) = stack.push(type).typeBindings
fun typeBindings(vararg types: Type) = stack(*types).typeBindings
fun bindings(type: Type, vararg types: Type) = nonEmptyStack(type, *types).typeBindings

val TypeBindings.asScript get() = stack.map { "bind" lineTo asScript }.script

fun TypeBindings.typeOrNull(argument: Argument): Type? =
	stack.drop(argument.previousStack)?.linkOrNull?.value

fun TypeBindings.typeOrError(argument: Argument): Type =
	typeOrNull(argument) ?: error("argument not bound")

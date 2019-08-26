package leo13

import leo9.*

data class TypeBindings(val stack: Stack<Type>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = stack.asScriptLine("bindings") { asScriptLine }
}

val Stack<Type>.typeBindings get() = TypeBindings(this)
fun TypeBindings.push(type: Type) = stack.push(type).typeBindings
fun typeBindings(vararg types: Type) = stack(*types).typeBindings
fun bindings(type: Type, vararg types: Type) = nonEmptyStack(type, *types).typeBindings

val TypeBindings.asScript get() = stack.map { "bind" lineTo asScript }.script

fun TypeBindings.typeOrNull(given: Given): Type? =
	stack.drop(given.previousStack)?.linkOrNull?.value

fun TypeBindings.typeOrError(given: Given): Type =
	typeOrNull(given) ?: error("argument not bound")

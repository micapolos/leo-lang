package leo13.type

import leo13.*
import leo9.*

data class TypeBindings(val stack: Stack<Type>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bindings"
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

val Stack<Type>.typeBindings get() = TypeBindings(this)
fun TypeBindings.push(type: Type) = stack.push(type).typeBindings
fun typeBindings(vararg types: Type) = stack(*types).typeBindings
fun bindings(type: Type, vararg types: Type) = nonEmptyStack(type, *types).typeBindings

val TypeBindings.asScript get() = stack.map { "bind" lineTo scriptableBody }.script

fun TypeBindings.typeOrNull(given: Given): Type? =
	stack.drop(given.previousStack)?.linkOrNull?.value

fun TypeBindings.typeOrError(given: Given): Type =
	typeOrNull(given) ?: error("argument not bound")

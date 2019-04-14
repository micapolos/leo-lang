package leo32.rt2

import leo.base.Empty
import leo32.base.Tree
import leo32.base.tree
import leo32.rt.errorSymbol

data class Scope(
	val typeTree: Tree<Type?>,
	val functionTree: Tree<Function?>,
	val argumentOrNull: Value?)

val Empty.scope
	get() =
		Scope(tree(), tree(), null)

val Scope.argument
	get() =
		argumentOrNull ?: value(errorSymbol)

fun Scope.invoke(value: Value) =
	emptyValue.invoke(value)
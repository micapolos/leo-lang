package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.orIfNull
import leo32.base.*
import leo32.interpreter.eval
import leo32.interpreter.macro

data class Scope(
	val functionTree: Tree<Function?>)

val Tree<Function?>.scope get() =
	Scope(this)

val Empty.scope get() =
	Scope(tree())

fun Scope.define(pair: Pair<Term, Function>) =
	functionTree
		.put32(pair.first.seq32 to pair.second)
		.scope

fun Scope.plus(field: TermField): Scope =
	functionTree
		.at32(field.seq32)
		.orIfNull { empty.tree() }
		.scope

fun Scope.plus(term: Term): Scope =
	fold(term.fieldSeq) { plus(it) }

fun Scope.eval(vararg fields: TermField) =
	value.fold(fields) { plus(it) }.term

val Scope.functionOrNull get() =
	if (functionTree is LeafTree && functionTree.leaf.value != null) functionTree.leaf.value
	else null

fun Scope.invoke(term: Term): Term =
	when (functionTree) {
		is LeafTree ->
			macro(term).eval.let { macroedTerm ->
				functionTree.leaf.value
					?.invoke(parameter(macroedTerm))
					?:macroedTerm
			}
		is BranchTree -> term
	}

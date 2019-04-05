package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.orIfNull
import leo32.base.Tree
import leo32.base.at32
import leo32.base.put32
import leo32.base.tree

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
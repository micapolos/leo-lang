package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.orIfNull
import leo32.base.*
import leo32.bitSeq

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
			term.evalMacros.let { termAfterMacros ->
				functionTree.leaf.value
					?.invoke(parameter(termAfterMacros))
					?:termAfterMacros
			}
		is BranchTree -> term
	}

fun Scope.define(type: Type): Scope =
	fold(type.eitherSeq) { either ->
		define(either.term.plus(term("type")) to type.gives(template(either.term)))
	}

fun Scope.function(term: Term) =
	functionTree
		.at(term.seq32.bitSeq)
		?.leafOrNull
		?.value
		?: term.rawType.gives(argument.template)

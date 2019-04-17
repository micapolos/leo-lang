package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo32.base.Tree
import leo32.base.at
import leo32.base.put
import leo32.base.tree

data class Functions(
	val functionTree: Tree<Function?>)

val Tree<Function?>.functions get() =
	Functions(this)

val Empty.functions get() =
	tree<Function>().functions

fun Functions.put(typeTerm: Term, function: Function) =
	functionTree
		.put(typeTerm.bitSeq, function)
		.functions

fun Functions.plus(field: TermField): Functions =
	functionTree.at(field.bitSeq)
		?.functions
		?:empty.functions
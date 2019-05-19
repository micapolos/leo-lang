package leo3

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.binary.Bit
import leo32.Dict
import leo32.at
import leo32.base.Tree
import leo32.base.at
import leo32.base.tree
import leo32.put

data class Scope(
	val templateTree: Tree<Template?>)

val Empty.scope
	get() = Scope(empty.tree())

fun Scope.matchAt(bit: Bit): Match? =
	templateTree.at(bit)?.let { templateTreeAtBit ->
		match(Scope(templateTreeAtBit))
	}

fun Scope.templateAt(line: Line): Template? =
	Dict<Line, Template>(templateTree) { bitSeq }.at(line)

fun Scope.put(value: Value, template: Template) =
	Scope(Dict<Value, Template>(templateTree) { bitSeq }.put(value, template).tree)

fun scope(vararg pairs: Pair<Value, Template>) =
	empty.scope.fold(pairs) { put(it.first, it.second) }

val Scope.completedBitReader
	get() =
		emptyValue.lineReader.completedTokenReader.completedWordReader.completedByteReader.completedBitReader

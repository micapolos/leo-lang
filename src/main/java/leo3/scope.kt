package leo3

import leo.base.Empty
import leo.base.empty
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

fun Scope.templateAt(value: Value): Template? =
	Dict<Value, Template>(templateTree) { bitSeq }.at(value)

fun Scope.put(value: Value, template: Template) =
	Scope(Dict<Value, Template>(templateTree) { bitSeq }.put(value, template).tree)

val Scope.completedBitReader
	get() =
		emptyValue.lineReader.completedTokenReader.completedWordReader.completedByteReader.completedBitReader

fun Scope.apply(value: Value) =
	templateAt(value)!!.apply(parameter(value))
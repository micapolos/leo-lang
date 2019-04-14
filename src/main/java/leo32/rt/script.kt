package leo32.rt

import leo.base.Empty
import leo.base.The
import leo.base.fail
import leo.base.the
import leo32.base.*
import leo32.base.List

data class Script(
	val fieldList: List<Field>,
	val symbolToTheValueTree: Tree<The<Value?>?>)

val Empty.script
	get() =
		Script(list(), tree())

fun Script.plus(field: Field) =
	Script(
		fieldList.add(field),
		symbolToTheValueTree.updateWithDefault(field.symbol.bitSeq, { null }) {
			when (this) {
				is LeafTree ->
					if (leaf.value == null) the(field.value)
					else the(null)
				is BranchTree -> fail()
			}.leaf.tree
		})

fun Script.at(symbol: Symbol): Value? =
	symbolToTheValueTree.at(symbol.bitSeq)?.leafOrNull?.value?.value

fun Scope.at(script: Script, symbol: Symbol): Value? =
	script.symbolToTheValueTree.at(symbol.bitSeq)?.leafOrNull?.value?.value

fun Scope.fieldSeq(script: Script) =
	script.fieldList.seq

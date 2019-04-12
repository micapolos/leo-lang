package leo32.runtime

import leo.base.Empty
import leo.base.fold
import leo.base.orIfNull
import leo32.base.BranchTree
import leo32.base.LeafTree

data class Scope(
	val valueToTypeDictionary: Dictionary<Term>,
	val typeToValueDictionary: Dictionary<Term>,
	val typeToTemplateDictionary: Dictionary<Template>)

val Empty.scope get() =
	Scope(dictionary(), dictionary(), dictionary())

fun Scope.define(termHasTerm: TermHasTerm): Scope =
	termHasTerm.rhs
		.listTermSeqOrNull("either")
		?.let { termSeq ->
			fold(termSeq) { eitherTerm ->
				copy(
					valueToTypeDictionary = valueToTypeDictionary.put(
						termHasTerm.lhs.leafPlus(eitherTerm),
						termHasTerm.lhs))
			}
		}.orIfNull {
			copy(valueToTypeDictionary = valueToTypeDictionary.put(
				termHasTerm.lhs.leafPlus(termHasTerm.rhs),
				termHasTerm.lhs))
		}.copy(typeToValueDictionary = typeToValueDictionary.put(
				termHasTerm.lhs,
				termHasTerm.lhs.leafPlus(termHasTerm.rhs)))

fun Scope.define(termGivesTerm: TermGivesTerm): Scope =
	copy(typeToTemplateDictionary = typeToTemplateDictionary.put(
		termGivesTerm.lhs,
		template(termGivesTerm.rhs)))

fun Scope.plus(field: TermField) =
	copy(
		valueToTypeDictionary = valueToTypeDictionary.plus(field),
		typeToValueDictionary = typeToValueDictionary.plus(field),
		typeToTemplateDictionary = typeToTemplateDictionary.plus(field))

fun Scope.invoke(parameter: Parameter): Term? =
	valueToTypeDictionary.tree.let { tree ->
		when (tree) {
			is BranchTree -> null
			is LeafTree ->
				if (tree.leaf.value == null) null
				else typeToTemplateDictionary
					.at(tree.leaf.value.value)
					?.let { template -> template.invoke(parameter) }
		}
	}

fun Scope.invoke(vararg lines: Line): Term =
	emptyTerm.fold(lines) { invoke(it) }
package leo.term

import leo.base.*

data class Selector(
	val getterStackOrNull: Stack<Getter>?) {
	override fun toString() = "selector ${getterStackOrNull.string}"
}

val Stack<Getter>?.selector
	get() =
		Selector(this)

val Selector.getterStreamOrNull: Stream<Getter>?
	get() =
		getterStackOrNull?.reverse?.stream

val thisSelector =
	nullOf<Stack<Getter>>().selector

fun Selector?.then(getter: Getter) =
	this?.getterStackOrNull.push(getter).selector

fun selector(vararg getters: Getter) =
	getters.fold(thisSelector, Selector::then)

// === invoke

fun Script.select(selector: Selector): Script? =
	orNull.fold(selector.getterStreamOrNull) { selectorItem ->
		this?.get(selectorItem)
	}

// === parse

//fun Script<Nothing>.parseSelector(pattern: Term<Pattern>): Selector? =
//	parseSelectorToPattern(pattern)?.first
//
//fun Script<Nothing>.parseSelectorToPattern(pattern: Term<Pattern>): Pair<Selector, Term<Pattern>>? =
//	matchWord(argumentWord) {
//		selector() to pattern
//	} ?: onlyFieldOrNull?.run {
//		value.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
//			if (pattern.structureTermOrNull?.isList == true)
//				when (key) {
//					lastWord -> pattern.select(key)?.let { argumentValue ->
//						selector.last to argumentValue
//					}
//					previousWord -> pattern.select(key)?.let { argumentValue ->
//						selector.previous to argumentValue
//					}
//					else -> null
//				}
//			else pattern.select(key)?.let { argumentValue ->
//				selector.get(key) to argumentValue
//			}
//		}
//	}

// === parsing

//fun Term<Nothing>.parseSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
//	parseSelector(patternTerm)?.meta?.term ?: parseNonSelectorTerm(patternTerm)
//
//fun Term<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
//	when (this) {
//		is MetaTerm -> fail
//		is WordTerm -> word.term()
//		is StructureTerm -> structure.parseNonSelectorTerm(patternTerm)
//	}
//
//fun Structure<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
//	fieldStream.run {
//		first.parseSelectorField(patternTerm).structure.fold(nextOrNull) { field ->
//			plus(field.parseSelectorField(patternTerm))
//		}.term
//	}
//
//fun Field<Nothing>.parseSelectorField(patternTerm: Term<Pattern>): Field<Selector> =
//	key fieldTo value.parseSelectorTerm(patternTerm)
//
//// === reflect
//
//val Selector.reflect: Field<Nothing>
//	get() =
//		selectorWord fieldTo (getterStreamOrNull?.reflect(Getter::reflect) ?: nullWord.term)
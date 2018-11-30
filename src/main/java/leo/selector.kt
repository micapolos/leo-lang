package leo

import leo.base.*

data class Selector(
	val getterStackOrNull: Stack<Getter>?)

val Stack<Getter>?.selector
	get() =
		Selector(this)

val Selector.selectorItemStreamOrNull: Stream<Getter>?
	get() =
		getterStackOrNull?.reverse?.stream

val thisSelector =
	nullOf<Stack<Getter>>().selector

fun Selector?.then(getter: Getter) =
	this?.getterStackOrNull.push(getter).selector

fun Selector.get(word: Word) =
	getterStackOrNull.push(WordGetter(word)).selector

val Selector.last: Selector
	get() =
		getterStackOrNull.push(LastGetter).selector

val Selector.previous: Selector
	get() =
		getterStackOrNull.push(PreviousGetter).selector

fun selector(vararg getters: Getter) =
	getters.fold(thisSelector, Selector::then)

// === parse

fun Term<Nothing>.parseSelector(pattern: Term<Pattern>): Selector? =
	parseSelectorToPattern(pattern)?.first

fun Term<Nothing>.parseSelectorToPattern(pattern: Term<Pattern>): Pair<Selector, Term<Pattern>>? =
	matchWord(argumentWord) {
		selector() to pattern
	} ?: onlyFieldOrNull?.run {
		value.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
			if (pattern.structureTermOrNull?.isList == true)
				when (key) {
					lastWord -> pattern.structureTermOrNull?.fieldStack?.top?.value?.let { argumentValue ->
						selector.last to argumentValue
					}
					previousWord -> pattern.structureTermOrNull?.fieldStack?.pop?.structureTerm?.let { argumentValue ->
						selector.previous to argumentValue
					}
					else -> null
				}
			else pattern.select(key)?.let { argumentValue ->
				selector.get(key) to argumentValue
			}
		}
	}

// === invoke

fun <V> Selector.invoke(argumentTerm: Term<V>): Term<V>? =
	argumentTerm.orNull.fold(selectorItemStreamOrNull) { selectorItem ->
		this?.get(selectorItem)
	}

fun <V> Term<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
	when (this) {
		is MetaTerm -> meta.invoke(argumentTerm)
		is WordTerm -> word.invoke()
		is StructureTerm -> this.invoke(argumentTerm)
	}

fun <V> Word.invoke(): Term<V>? =
	term()

fun <V> Meta<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
	value.invoke(argumentTerm)

fun <V> StructureTerm<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
	fieldStream.run {
		first.invoke(argumentTerm)?.onlyTerm.fold(nextOrNull) { field ->
			field.invoke(argumentTerm)?.let { invokedField ->
				this?.push(invokedField)
			}
		}
	}

fun <V> Field<Selector>.invoke(argumentTerm: Term<V>): Field<V>? =
	value.invoke(argumentTerm)?.let { invokedTerm ->
		key fieldTo invokedTerm
	}

// === parsing

fun Term<Nothing>.parseSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	parseSelector(patternTerm)?.meta?.term ?: parseNonSelectorTerm(patternTerm)

fun Term<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	when (this) {
		is MetaTerm -> fail
		is WordTerm -> word.term()
		is StructureTerm -> structure.parseNonSelectorTerm(patternTerm)
	}

fun Structure<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	fieldStream.run {
		first.parseSelectorField(patternTerm).structure.fold(nextOrNull) { field ->
			plus(field.parseSelectorField(patternTerm))
		}.term
	}

fun Field<Nothing>.parseSelectorField(patternTerm: Term<Pattern>): Field<Selector> =
	key fieldTo value.parseSelectorTerm(patternTerm)

// === reflect

val Selector.reflect: Field<Nothing>
	get() =
		selectorWord fieldTo (selectorItemStreamOrNull?.reflect(Getter::reflect) ?: nullWord.term)
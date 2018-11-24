package leo

import leo.base.*

data class Selector(
	val wordStackOrNull: Stack<Word>?) {
	override fun toString() = reflect.string
}

val Stack<Word>?.selector
	get() =
		Selector(this)

fun Selector.then(word: Word) =
	wordStackOrNull.push(word).selector

fun selector(vararg words: Word) =
	stackOrNull(*words).selector

val Selector.wordStreamOrNull: Stream<Word>?
	get() =
		wordStackOrNull?.reverse?.stream

fun Term<Nothing>.parseSelector(pattern: Term<Pattern>): Selector? =
	parseSelectorToPattern(pattern)?.first

fun Term<Nothing>.parseSelectorToPattern(pattern: Term<Pattern>): Pair<Selector, Term<Pattern>>? =
	matchWord(thisWord) {
		selector() to pattern
	} ?: onlyFieldOrNull?.run {
		value.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
			pattern.select(key)?.let { argumentValue ->
				selector.then(key) to argumentValue
			}
		}
	}

// === apply

fun Term<Selector>.apply(argument: Term<Nothing>): Term<Nothing> =
	invoke(argument)!!

// === invoke

fun <V> Selector.invoke(argumentTerm: Term<V>): Term<V>? =
	argumentTerm.orNull.fold(wordStreamOrNull) { word ->
		this?.select(word)
	}

fun <V> Term<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
	when (this) {
		is MetaTerm -> this.invoke(argumentTerm)
		is WordTerm -> this.invoke()
		is FieldsTerm -> this.invoke(argumentTerm)
	}

fun <V> MetaTerm<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
	value.invoke(argumentTerm)

fun <V> WordTerm<Selector>.invoke(): Term<V>? =
	word.term()

fun <V> FieldsTerm<Selector>.invoke(argumentTerm: Term<V>): Term<V>? =
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
	parseSelector(patternTerm)?.metaTerm ?: parseNonSelectorTerm(patternTerm)

fun Term<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	when (this) {
		is MetaTerm -> fail
		is WordTerm -> parseNonSelectorTerm()
		is FieldsTerm -> parseNonSelectorTerm(patternTerm)
	}

fun WordTerm<Nothing>.parseNonSelectorTerm(): Term<Selector> =
	word.term

fun FieldsTerm<Nothing>.parseNonSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	fieldStream.run {
		first.parseSelectorField(patternTerm).term.fold(nextOrNull) { field ->
			fieldsPush(field.parseSelectorField(patternTerm))
		}
	}

fun Field<Nothing>.parseSelectorField(patternTerm: Term<Pattern>): Field<Selector> =
	key fieldTo value.parseSelectorTerm(patternTerm)

// === reflect

val Selector.reflect: Field<Nothing>
	get() =
		selectorWord.fieldTo(wordStreamOrNull?.reflect(Word::reflect) ?: thisWord.term)
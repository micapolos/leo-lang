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

fun Term<Nothing>.parseSelector(pattern: Term<Pattern>): Selector? =
	parseSelectorToPattern(pattern)?.first

fun Term<Nothing>.parseSelectorToPattern(pattern: Term<Pattern>): Pair<Selector, Term<Pattern>?>? =
	if (this == thisWord.term) selector() to pattern
	else structureTermOrNull?.run {
		rhsTermOrNull?.let { term ->
			term.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
				pattern?.select(word)?.let { argumentValue ->
					selector.then(word) to argumentValue.value
				}
			}
		}
	}

// === apply

fun Term<Selector>?.apply(argumentOrNull: Term<Nothing>?): Term<Nothing>? =
	if (this == null) null
	else invoke(argumentOrNull)!!.value

// === invoke

fun <V> Selector.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	argumentTermOrNull.the.orNull.fold(wordStackOrNull?.reverse?.stream) { word ->
		this?.value?.select(word)
	}

fun <V> Term<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	when (this) {
		is Term.Meta -> this.invoke(argumentTermOrNull)
		is Term.Structure -> this.invoke(argumentTermOrNull)
	}

fun <V> Term.Meta<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	value.invoke(argumentTermOrNull)

fun <V> Term.Structure<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	fieldStream
		.reverse
		.foldFirst { field -> field.invoke(argumentTermOrNull)?.onlyStack }
		.foldNext { field ->
			field.invoke(argumentTermOrNull)?.let { invokedField ->
				push(invokedField)
			}
		}
		?.term
		?.the

fun <V> Field<Selector>.invoke(argumentTermOrNull: Term<V>?): Field<V>? =
	if (termOrNull == null) word.field
	else termOrNull.invoke(argumentTermOrNull)?.let { theValue ->
		word.fieldTo(theValue.value)
	}

// === parsing

fun Term<Nothing>.parseSelectorTerm(patternTerm: Term<Pattern>): Term<Selector> =
	parseSelector(patternTerm)?.metaTerm
		?: structureTermOrNull?.fieldStream?.reverse
			?.foldFirst { field -> field.parseSelectorField(patternTerm).onlyStack }
			?.foldNext { field -> push(field.parseSelectorField(patternTerm)) }
			?.term ?: fail

fun Field<Nothing>.parseSelectorField(patternTerm: Term<Pattern>): Field<Selector> =
	word fieldTo termOrNull?.parseSelectorTerm(patternTerm)

// === reflect

val Selector.reflect: Field<Nothing>
	get() =
		selectorWord
			.fieldTo(
				wordStackOrNull
					?.reflect(Word::reflect)
					?: thisWord.term)
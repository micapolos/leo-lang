package leo.lab

import leo.Word
import leo.base.*
import leo.selectorWord
import leo.thisWord

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

fun <V> Selector.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	argumentTermOrNull.the.orNull.fold(wordStackOrNull?.reverse?.stream) { word ->
		this?.value?.select(word)
	}

fun Term<Nothing>.parseSelector(choice: Term<Choice>): Selector? =
	parseSelectorToChoice(choice)?.first

fun Term<Nothing>.parseSelectorToChoice(choice: Term<Choice>): Pair<Selector, Term<Choice>?>? =
	if (this == thisWord.term) selector() to choice
	else structureTermOrNull?.run {
		fieldStack.top.termOrNull?.let { term ->
			term.parseSelectorToChoice(choice)?.let { (selector, choice) ->
				choice?.select(fieldStack.top.word)?.let { argumentValue ->
					selector.then(fieldStack.top.word) to argumentValue.value
				}
			}
		}
	}

// === application

fun Term<Selector>?.apply(argumentOrNull: Term<Nothing>?): Term<Nothing>? =
	this?.invoke(argumentOrNull)!!.value

fun <V> Term<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	when (this) {
		is Term.Meta -> this.invoke(argumentTermOrNull)
		is Term.Structure -> this.invoke(argumentTermOrNull)
	}

fun <V> Term.Meta<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	this.value.invoke(argumentTermOrNull)

fun <V> Term.Structure<Selector>.invoke(argumentTermOrNull: Term<V>?): The<Term<V>?>? =
	fieldStack
		.reverse
		.stream
		.foldFirst { field -> field.invoke(argumentTermOrNull)?.onlyStack }
		.foldNext { field ->
			field.invoke(argumentTermOrNull)?.let { invokedField ->
				push(invokedField)
			}
		}
		?.term
		?.the

fun <V> Field<Selector>.invoke(argumentTermOrNull: Term<V>?): Field<V>? =
	termOrNull?.invoke(argumentTermOrNull)?.let { theValue ->
		word.fieldTo(theValue.value)
	}

// === parsing

fun Term<Nothing>.parseSelectorTerm(choiceTerm: Term<Choice>): Term<Selector> =
	parseSelector(choiceTerm)?.metaTerm
		?: structureTermOrNull?.fieldStack?.reverse?.stream
			?.foldFirst { field -> field.parseSelectorField(choiceTerm).onlyStack }
			?.foldNext { field -> push(field.parseSelectorField(choiceTerm)) }
			?.term ?: fail

fun Field<Nothing>.parseSelectorField(choiceTerm: Term<Choice>): Field<Selector> =
	word fieldTo termOrNull?.parseSelectorTerm(choiceTerm)

// === reflect

val Selector.reflect: Field<Nothing>
	get() =
		selectorWord
			.fieldTo(
				wordStackOrNull
					?.reflect(Word::reflect)
					?: thisWord.term)
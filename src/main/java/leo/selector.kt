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

fun <V> Selector.invoke(argument: Term<V>): Term<V>? =
	argument.orNull.fold(wordStackOrNull?.reverse) { word ->
		this?.select(word)
	}

fun Term<Value>.parseSelector(pattern: Term<Pattern>): Selector? =
	parseSelectorToPattern(pattern)?.first

fun Term<Value>.parseSelectorToPattern(pattern: Term<Pattern>): Pair<Selector, Term<Pattern>>? =
	when (this) {
		is Term.Identifier ->
			if (word == thisWord) (selector() to pattern)
			else null
		is Term.Structure ->
			if (fieldStack.pop != null) null
			else fieldStack.top.value.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
				pattern.select(fieldStack.top.key)?.let { argumentValue ->
					selector.then(fieldStack.top.key) to argumentValue
				}
			}
		is Term.Meta -> null
	}


fun Term<Selector>.apply(script: Term<Value>): Term<Value> =
	invoke(script)!!

fun <V> Term<Selector>.invoke(argument: Term<V>): Term<V>? =
	when (this) {
		is Term.Meta -> this.invoke(argument)
		is Term.Identifier -> this.invoke()
		is Term.Structure -> this.invoke(argument)
	}

fun <V> Term.Meta<Selector>.invoke(argument: Term<V>): Term<V>? =
	value.invoke(argument)

fun <V> Term.Identifier<Selector>.invoke(): Term<V> =
	term(word)

fun <V> Term.Structure<Selector>.invoke(argument: Term<V>): Term<V>? =
	fieldStack.reverse.foldTop { field ->
		field.invoke(argument)?.stack
	}.andPop { stack, field ->
		field.invoke(argument)?.let { invokedField ->
			stack.push(invokedField)
		}
	}?.term

fun <V> Field<Selector>.invoke(argument: Term<V>): Field<V>? =
	value.invoke(argument)?.let { value ->
		key fieldTo value
	}

// === script parsing

fun Term<Value>.parseSelectorTerm(pattern: Term<Pattern>): Term<Selector> =
	parseSelector(pattern)?.metaTerm ?: when (this) {
		is Term.Identifier -> term(word)
		is Term.Structure ->
			fieldStack.reverse
				.foldTop { it.parseSelectorField(pattern).stack }
				.andPop { stack, field -> stack.push(field.parseSelectorField(pattern)) }
				.term
		is Term.Meta -> fail
	}

fun Field<Value>.parseSelectorField(pattern: Term<Pattern>): Field<Selector> =
	key fieldTo value.parseSelectorTerm(pattern)

// === reflect

val Selector.reflect: Field<Value>
	get() =
		selectorWord
			.fieldTo(
				wordStackOrNull
					?.reflect(Word::reflect)
					?: term(thisWord))
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
    wordStackOrNull?.reverse.fold(argument as Term<V>?) { foldedArgument, word ->
      foldedArgument?.only(word)
    }

fun Script.parseSelector(pattern: Pattern): Selector? =
    parseSelectorToPattern(pattern)?.first

fun Script.parseSelectorToPattern(pattern: Pattern): Pair<Selector, Pattern>? =
    when (term) {
      is Term.Identifier ->
        if (term.word == thisWord) (selector() to pattern)
        else null
      is Term.Structure ->
        if (term.fieldStack.pop != null) null
        else term.fieldStack.top.value.script.parseSelectorToPattern(pattern)?.let { (selector, pattern) ->
          pattern.term.only(term.fieldStack.top.key)?.let { argumentValue ->
            selector.then(term.fieldStack.top.key) to argumentValue.pattern
          }
        }
      is Term.Meta -> null
    }

// === reflect

val Selector.reflect: Field<Nothing>
  get() =
    selectorWord
        .fieldTo(
            wordStackOrNull
                ?.reflect(Word::reflect)
	            ?: term(thisWord))
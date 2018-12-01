package leo

sealed class Getter

data class WordGetter(
	val word: Word) : Getter()

object LastGetter : Getter()

object PreviousGetter : Getter()

val Word.getter: Getter
	get() =
		WordGetter(this)

val lastGetter: Getter
	get() =
		LastGetter

val previousGetter: Getter
	get() =
		PreviousGetter

fun <V> Term<V>.get(selectorItem: Getter): Term<V>? =
	when (selectorItem) {
		is WordGetter -> select(selectorItem.word)
		is LastGetter -> structureTermOrNull?.fieldStack?.head?.value
		is PreviousGetter -> structureTermOrNull?.fieldStack?.tail?.structureTerm
	}

val Getter.reflect: Field<Nothing>
	get() =
		getWord fieldTo when (this) {
			is WordGetter -> word.reflect.term
			is LastGetter -> topWord.term
			is PreviousGetter -> popWord.term
		}

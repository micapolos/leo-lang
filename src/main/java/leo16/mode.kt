package leo16

import leo16.names.*

// TODO: This is a temporary solution.
enum class Mode {
	EVALUATE,
	META,
	TYPE,
	QUOTE;

	override fun toString() = asField.toString()
}

val Mode.asField: Field
	get() =
		_mode(name.toLowerCase()())

fun Mode.begin(word: String): Mode =
	when (this) {
		Mode.EVALUATE ->
			when (word) {
				_expands -> Mode.QUOTE
				_gives -> Mode.QUOTE
				_giving -> Mode.QUOTE
				_give -> Mode.QUOTE
				_function -> Mode.TYPE
				_match -> Mode.QUOTE
				_quote -> Mode.QUOTE
				_test -> Mode.QUOTE
				else -> this
			}
		Mode.TYPE ->
			when (word) {
				_gives -> Mode.QUOTE
				else -> this
			}
		Mode.META -> Mode.EVALUATE.begin(word)
		Mode.QUOTE -> this
	}

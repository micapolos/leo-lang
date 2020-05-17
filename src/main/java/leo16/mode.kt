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
				_does -> Mode.QUOTE
				_giving -> Mode.QUOTE
				_do -> Mode.QUOTE
				_function -> Mode.TYPE
				_lazy -> Mode.QUOTE
				_match -> Mode.QUOTE
				_quote -> Mode.QUOTE
				_script -> Mode.QUOTE
				_test -> Mode.QUOTE
				_use -> Mode.QUOTE
				_word -> Mode.META
				else -> this
			}
		Mode.TYPE ->
			when (word) {
				_does -> Mode.QUOTE
				else -> this
			}
		Mode.META -> Mode.EVALUATE.begin(word)
		Mode.QUOTE -> this
	}

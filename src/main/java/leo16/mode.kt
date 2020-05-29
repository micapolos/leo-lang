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
				_meta -> Mode.META
				_quote -> Mode.QUOTE
				_test -> Mode.QUOTE
				_use -> Mode.QUOTE
				else -> this
			}
		Mode.TYPE ->
			when (word) {
				_does -> Mode.QUOTE
				else -> this
			}
		Mode.META -> Mode.EVALUATE
		Mode.QUOTE -> this
	}

val Value.modeOrNull: Mode?
	get() =
		matchPrefix(_mode) { rhs ->
			rhs.matchWord { word ->
				when (word) {
					_evaluate -> Mode.EVALUATE
					_quote -> Mode.QUOTE
					_meta -> Mode.META
					_type -> Mode.TYPE
					else -> null
				}
			}
		}
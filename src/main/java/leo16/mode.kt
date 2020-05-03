package leo16

import leo15.*

// TODO: This is a temporary solution.
enum class Mode {
	EVALUATE,
	QUOTE;

	override fun toString() = asField.toString()
}

val Mode.asField: Field
	get() =
		modeName(name.toLowerCase()())

fun Mode.begin(word: String): Mode =
	when (this) {
		Mode.EVALUATE ->
			when (word) {
				givesName -> Mode.QUOTE
				quoteName -> Mode.QUOTE
				testName -> Mode.QUOTE
				else -> this
			}
		Mode.QUOTE -> this
	}
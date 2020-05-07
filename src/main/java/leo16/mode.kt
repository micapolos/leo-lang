package leo16

import leo15.*

// TODO: This is a temporary solution.
enum class Mode {
	EVALUATE,
	META,
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
				givingName -> Mode.QUOTE
				giveName -> Mode.QUOTE
				quoteName -> Mode.QUOTE
				testName -> Mode.QUOTE
				//listName -> Mode.META
				//choiceName -> Mode.META
				else -> this
			}
		Mode.META -> Mode.EVALUATE.begin(word)
		Mode.QUOTE -> this
	}

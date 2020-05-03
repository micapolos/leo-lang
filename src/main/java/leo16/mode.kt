package leo16

import leo15.*

// TODO: This is a temporary solution.
enum class Mode {
	EVALUATE,
	DEFINE,
	QUOTE;

	override fun toString() = asField.toString()
}

val Mode.asField: Field
	get() =
		modeName(name.toLowerCase()())

fun Mode.begin(mode: Mode): Mode =
	when (this) {
		Mode.EVALUATE -> mode
		Mode.DEFINE -> this // What about mode == QUOTE?
		Mode.QUOTE -> this
	}

val String.mode: Mode
	get() =
		when (this) {
			defineName -> Mode.DEFINE
			givesName -> Mode.QUOTE
			givingName -> Mode.QUOTE
			dictionaryName -> Mode.QUOTE
			matchName -> Mode.QUOTE
			quoteName -> Mode.QUOTE
			else -> Mode.EVALUATE
		}
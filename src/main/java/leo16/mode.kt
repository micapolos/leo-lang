package leo16

import leo15.*

// TODO: This is a temporary solution.
enum class Mode {
	EVALUATE,
	DEFINE,
	NORMALIZE,
	QUOTE;

	override fun toString() = asField.toString()
}

val Mode.asField: Field
	get() =
		modeName(name.toLowerCase()())

fun Mode.begin(mode: Mode): Mode =
	when (this) {
		Mode.EVALUATE -> mode
		Mode.DEFINE -> this
		Mode.NORMALIZE -> this
		Mode.QUOTE -> this
	}

val String.mode: Mode
	get() =
		when (this) {
			defineName -> Mode.NORMALIZE
			givesName -> Mode.QUOTE
			givingName -> Mode.QUOTE
			dictionaryName -> Mode.NORMALIZE
			matchName -> Mode.NORMALIZE
			quoteName -> Mode.QUOTE
			else -> Mode.EVALUATE
		}
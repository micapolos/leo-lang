package leo

// TODO: Better name?
sealed class Control

data class BeginControl(
	val begin: Begin) : Control() {
	override fun toString() = beginString
}

data class EndControl(
	val end: End) : Control() {
	override fun toString() = endString
}

val Begin.control: Control
	get() =
		BeginControl(this)

val End.control: Control
	get() =
		EndControl(this)

val Control.character: Character
	get() =
		when (this) {
			is BeginControl -> begin.character
			is EndControl -> end.character
		}

val Control.reflect: Field<Nothing>
	get() =
		controlWord fieldTo
			when (this) {
				is BeginControl -> beginWord.term
				is EndControl -> endWord.term
			}

val Field<Nothing>.parseControl
	get() =
		matchKey(controlWord) {
			matchWord(beginWord) { begin.control } ?: matchWord(endWord) { end.control }
		}
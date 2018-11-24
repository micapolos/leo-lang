package leo

// Find a better name: transition? parenthesis? closure? block?
sealed class Control

data class BeginControl(
	val begin: Begin): Control() {
	override fun toString() = beginString
}

data class EndControl(
	val end: End): Control() {
	override fun toString() = endString
}

val Begin.control: Control
  get() =
	  BeginControl(this)

val End.control: Control
	get() =
		EndControl(this)


package leo32

sealed class Match

data class PartialMatch(
	val function: Function) : Match()

data class OpMatch(
	val op: Op) : Match()

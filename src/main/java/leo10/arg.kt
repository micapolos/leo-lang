package leo10

sealed class Arg

data class LastArg(
	val last: Last) : Arg()

data class PreviousArg(
	val previous: ArgPrevious) : Arg()

data class ArgPrevious(
	val arg: Arg)


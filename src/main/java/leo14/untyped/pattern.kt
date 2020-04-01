package leo14.untyped

data class Pattern(val thunk: Thunk)

fun pattern(thunk: Thunk) = Pattern(thunk)

fun Pattern.matches(thunk: Thunk) =
	this.thunk.matches(thunk)

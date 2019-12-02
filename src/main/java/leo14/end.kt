package leo14

object End {
	override fun toString() = ")"
}

val end = End

val End.reflectScriptLine
	get() =
		"end".line
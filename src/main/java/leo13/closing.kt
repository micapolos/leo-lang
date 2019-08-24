package leo13

object Closing {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "closing" lineTo script()
}

val closing = Closing

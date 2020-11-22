package leo21.type

object Text : AsLine {
	override val asLine get() = stringLine
}

val text = Text
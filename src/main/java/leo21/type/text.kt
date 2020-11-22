package leo21.type

object Text : TypeComponent {
	override val typeComponentLine get() = stringLine
}

val text = Text
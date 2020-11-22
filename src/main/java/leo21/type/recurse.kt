package leo21.type

data class Recurse(val index: Int) : TypeComponent {
	override val typeComponentLine get() = line(this)
}

fun recurse(index: Int) = Recurse(index)

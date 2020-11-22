package leo21.type

data class Recurse(val index: Int) : AsLine {
	override val asLine get() = line(this)
}

fun recurse(index: Int) = Recurse(index)

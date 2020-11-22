package leo21.type

data class Recursive(val line: Line) : TypeComponent {
	override val typeComponentLine get() = line(this)
}

fun recursive(line: Line) = Recursive(line)
val Line.asRecursive get() = recursive(this)

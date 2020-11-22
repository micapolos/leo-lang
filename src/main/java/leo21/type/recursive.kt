package leo21.type

data class Recursive(val line: Line) : AsLine {
	override val asLine get() = line(this)
}

fun recursive(line: Line) = Recursive(line)
val Line.asRecursive get() = recursive(this)

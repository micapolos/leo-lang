package leo21.type

data class Recursive(val line: Line)

fun recursive(line: Line) = Recursive(line)
val Line.asRecursive get() = recursive(this)

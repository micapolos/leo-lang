package leo21.type

sealed class Line
object StringLine : Line()
object DoubleLine : Line()
data class FieldLine(val field: Field) : Line()
data class ArrowLine(val arrow: Arrow) : Line()

val Line.arrowOrNull: Arrow? get() = (this as? ArrowLine)?.arrow
val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field

fun line(arrow: Arrow): Line = ArrowLine(arrow)
fun line(field: Field): Line = FieldLine(field)

infix fun String.lineTo(rhs: Type) = line(this fieldTo rhs)
infix fun Type.lineTo(rhs: Type) = line(this arrowTo rhs)

val Line.name: String
	get() =
		when (this) {
			StringLine -> "text"
			DoubleLine -> "number"
			is FieldLine -> field.name
			is ArrowLine -> "function"
		}

val stringLine: Line = StringLine
val doubleLine: Line = DoubleLine

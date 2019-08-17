package leo13

data class TypedValue(val value: Value, val type: Type)
data class TypedValueLine(val name: String, val rhs: TypedValue)

infix fun Value.of(type: Type) = TypedValue(this, type)
infix fun String.lineTo(rhs: TypedValue) = TypedValueLine(this, rhs)
fun typedValue() = value() of type()

val TypedValue.script get() = type.script(value)
val TypedValue.typedScript get() = type.script(value) of type

fun TypedValue.plus(line: TypedValueLine): TypedValue =
	value.plus(line.valueLine) of type.plus(line.typeLine)

val TypedValueLine.valueLine get() = 0 lineTo rhs.value
val TypedValueLine.typeLine get() = name lineTo rhs.type

val TypedValue.typedExpr get() = value.expr of type

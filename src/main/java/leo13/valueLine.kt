package leo13

data class ValueLine(
	val name: String,
	val rhs: Value) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = name lineTo rhs.asScriptLine.rhs
}

fun valueLine(name: String, rhs: Value = value()) = ValueLine(name, rhs)
infix fun String.lineTo(value: Value) = valueLine(this, value)

val ScriptLine.valueLine: ValueLine get() = name lineTo rhs.value
val ValueLine.scriptLineOrNull: ScriptLine? get() = rhs.scriptOrNull?.let { name lineTo it }
package leo13

data class TypedScript(val script: Script, val type: Type)
data class TypedScriptLine(val name: String, val rhs: TypedScript)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun String.lineTo(rhs: TypedScript) = TypedScriptLine(this, rhs)

val TypedScript.value get() = type.value(script)
val TypedScript.code get() = "${script.code} : $type"

fun TypedScript.plus(line: TypedScriptLine) =
	script.plus(line.name lineTo line.rhs.script) of type.plus(line.name lineTo line.rhs.type)
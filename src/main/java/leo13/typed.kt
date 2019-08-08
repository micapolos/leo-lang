package leo13

data class TypedScript(val script: Script, val type: Type)

infix fun Script.of(type: Type) = TypedScript(this, type)

val TypedScript.value get() = type.value(script)

val TypedScript.code get() = "${script.code} : $type"

package leo13

data class TypedScript(val script: Script, val type: Type)
data class TypedExpr(val expr: Expr, val type: Type)
data class TypedValue(val value: Value, val type: Type)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun Value.of(type: Type) = TypedValue(this, type)

val TypedScript.value get() = type.value(script)
val TypedValue.script get() = type.script(value)

val TypedScript.code get() = "${script.code} : $type"

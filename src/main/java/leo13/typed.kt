package leo13

data class TypedScript(val script: Script, val type: Type)
data class TypedExpr(val expr: Expr, val type: Type)
data class TypedValue(val value: Value, val type: Type)

data class TypedExprLine(val name: String, val rhs: TypedExpr)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun Value.of(type: Type) = TypedValue(this, type)

infix fun String.lineTo(rhs: TypedExpr) = TypedExprLine(this, rhs)

val TypedScript.value get() = type.value(script)
val TypedValue.script get() = type.script(value)

val TypedScript.code get() = "${script.code} : $type"

// --- normalization

fun TypedExpr.plusNormalized(line: TypedExprLine) =
	if (line.rhs.type.isEmpty) expr(op(opLink(0 lineTo expr))) of type(choice(line.name lineTo type))
	else plus(line)

fun TypedExpr.plus(line: TypedExprLine) =
	expr.plus(op(opLink(0 lineTo line.rhs.expr))) of type.plus(choice(line.name lineTo line.rhs.type))

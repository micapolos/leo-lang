package leo13

data class TypedExpr(val expr: Expr, val type: Type)
data class TypedExprLine(val name: String, val rhs: TypedExpr)

// --- constructors

infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun String.lineTo(rhs: TypedExpr) = TypedExprLine(this, rhs)

// --- normalization

fun TypedExpr.plusNormalized(line: TypedExprLine) =
	if (line.rhs.type.isEmpty) expr(op(opLink(0 lineTo expr))) of type(choice(line.name lineTo type))
	else plus(line)

fun TypedExpr.plus(line: TypedExprLine) =
	expr.plus(op(opLink(0 lineTo line.rhs.expr))) of type.plus(choice(line.name lineTo line.rhs.type))

// --- script -> typed expr

fun Script.typedExpr(context: Context): TypedExpr =
	null
		?: argumentTypedExprOrNull(context)
		?: callTypedExprOrNull(context)
		?: scriptTypedExpr(context)

fun Script.argumentTypedExprOrNull(context: Context): TypedExpr? =
	argumentOrNull?.let { argument ->
		context.bindings.typedExprOrNull(argument)
	}

fun Script.callTypedExprOrNull(context: Context): TypedExpr? =
	null // TODO()

fun Script.scriptTypedExpr(context: Context): TypedExpr =
	linkOrNull
		?.let { link ->
			link.lhs.typedExpr(context).plus(link.line.typedExprLine(context))
		}
		?: expr() of type()

fun ScriptLine.typedExprLine(context: Context): TypedExprLine =
	name lineTo rhs.typedExpr(context)

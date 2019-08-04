package leo13

import leo.base.notNullIf

data class TypedExpr(val expr: Expr, val type: Type)
data class TypedExprLink(val lhs: TypedExpr, val line: TypedExprLine)
data class TypedExprLine(val name: String, val rhs: TypedExpr)
data class TypedExprScriptLink(val lhs: TypedExpr, val line: ScriptLine)

// --- constructors

infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun String.lineTo(rhs: TypedExpr) = TypedExprLine(this, rhs)
infix fun TypedExpr.linkTo(line: TypedExprLine) = TypedExprLink(this, line)
infix fun TypedExpr.linkTo(line: ScriptLine) = TypedExprScriptLink(this, line)

// --- normalization

fun TypedExpr.plusNormalized(line: TypedExprLine) =
	if (line.rhs.type.isEmpty) expr(op(opLink(0 lineTo expr))) of type(choice(line.name lineTo type))
	else plus(line)

fun TypedExpr.plus(line: TypedExprLine) =
	expr.plus(op(opLink(0 lineTo line.rhs.expr))) of type.plus(choice(line.name lineTo line.rhs.type))

val TypedExprLink.normalizedLineOrNull
	get() =
		if (lhs.type.isEmpty) notNullIf(!line.rhs.type.isEmpty) { line }
		else notNullIf(line.rhs.type.isEmpty) { line.name lineTo lhs }

// --- script -> typed expr

fun Script.typedExpr(context: Context): TypedExpr =
	null
		?: argumentTypedExprOrNull(context)
		?: switchTypedExprOrNull(context)
		?: accessTypedExprOrNull(context)
		?: callTypedExprOrNull(context)
		?: scriptTypedExpr(context)

fun Script.argumentTypedExprOrNull(context: Context): TypedExpr? =
	argumentOrNull?.let { argument ->
		context.bindings.typedExprOrNull(argument)
	}

fun Script.switchTypedExprOrNull(context: Context) =
	null //TODO()

fun Script.accessTypedExprOrNull(context: Context): TypedExpr? =
	linkLineOrNull?.let { linkLine ->
		linkLine.rhs.script.typedExpr(context).accessOrNull(linkLine.name)
	}

fun Script.callTypedExprOrNull(context: Context): TypedExpr? =
	linkOrNull?.let { link ->
		link.lhs.typedExpr(context).let { lhsTypedExpr ->
			link.line.rhs.typedExpr(context).let { rhsTypedExpr ->
				lhsTypedExpr.expr.plus(0 lineTo rhsTypedExpr.expr).let { expr ->
					lhsTypedExpr.type.plus(link.line.name lineTo rhsTypedExpr.type).let { type ->
						context.scope.typedExprOrNull(parameter(type))?.let { typedExpr ->
							expr.plus(op(call(typedExpr.expr))) of typedExpr.type
						}
					}
				}
			}
		}
	}

fun Script.scriptTypedExpr(context: Context): TypedExpr =
	linkOrNull
		?.let { link ->
			link.lhs.typedExpr(context).plus(link.line.typedExprLine(context))
		}
		?: expr() of type()

fun ScriptLine.typedExprLine(context: Context): TypedExprLine =
	name lineTo rhs.typedExpr(context)

// --- access

fun TypedExpr.accessOrNull(name: String) =
	type.accessOrNull(name)?.let { access ->
		expr.plus(op(access(access.int))) of access.type
	}

// --- argument

val TypedExprLink.argumentOrNull
	get() =
		notNullIf(lhs.type.isEmpty && line.name == "argument" && line.rhs.type.isEmpty) {
			argument()
		}

val TypedExprLink.accessTypedExprOrNull
	get() =
		normalizedLineOrNull?.let { line ->
			line.rhs.type.accessOrNull(line.name)?.let { access ->
				line.rhs.expr.plus(op(access(access.int))) of access.type
			}
		}

val TypedExprLink.typedExpr
	get() =
		lhs.plus(line)

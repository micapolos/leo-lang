package leo13

import leo.base.ifOrNull
import leo9.fold
import leo9.push
import leo9.reverse

data class Interpreter(
	val context: Context,
	val typedExpr: TypedExpr)

fun interpreter(context: Context, typedExpr: TypedExpr) =
	Interpreter(context, typedExpr)

fun interpreter() = interpreter(context(), expr() of type())

fun Interpreter.push(script: Script): Interpreter =
	fold(script.lineStack.reverse, Interpreter::push)

fun Interpreter.push(line: ScriptLine) =
	push(line.typedExprLine(context))

fun Interpreter.push(line: TypedExprLine): Interpreter =
	null
		?: pushDefineOrNull(line)
		?: pushEval(line)

fun Interpreter.pushDefineOrNull(line: TypedExprLine) =
	pushDefineRhsOrNull(line) ?: pushLhsDefineOrNull(line)

fun Interpreter.pushDefineRhsOrNull(line: TypedExprLine) =
	ifOrNull(typedExpr.type.isEmpty) {
		context.defineInterpreterOrNull(line.rhs)
	}

fun Interpreter.pushLhsDefineOrNull(line: TypedExprLine): Interpreter? =
	ifOrNull(line.rhs.type.isEmpty) {
		context.defineInterpreterOrNull(typedExpr)
	}

fun Context.defineInterpreterOrNull(typedExpr: TypedExpr): Interpreter? =
	typedExpr.type.onlyFunctionTypeOrNull?.let { functionType ->
		interpreter(
			context(
				scope
					.functionStack
					.push(
						function(
							parameter(functionType.parameter),
							typedExpr.expr of functionType.result))
					.scope,
				bindings),
			expr() of type())
	}

fun Interpreter.pushEval(line: TypedExprLine): Interpreter =
	TODO()
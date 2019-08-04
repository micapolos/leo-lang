package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.*

data class Interpreter(
	val context: Context,
	val typedExpr: TypedExpr)

fun interpreter(context: Context, typedExpr: TypedExpr) =
	Interpreter(context, typedExpr)

fun interpreter() = interpreter(context(), expr() of type())

fun Interpreter.push(script: Script): Interpreter =
	fold(script.lineStack.reverse) { push(it) }

fun Interpreter.push(line: ScriptLine): Interpreter =
	when (line.name) {
		"case" -> pushCase(line.rhs)
		"gives" -> pushGives(line.rhs)
		"switch" -> pushSwitch(line.rhs)
		else -> push(line.typedExprLine(context))
	}

fun Interpreter.pushCase(rhs: Script): Interpreter = TODO()

fun Interpreter.pushGives(rhs: Script): Interpreter =
	interpreter(
		context.plus(
			function(
				parameter(typedExpr.type),
				context.bind(typedExpr).typedExpr(rhs))),
		expr() of type())

fun Interpreter.pushSwitch(rhs: Script): Interpreter =
	switchOrNull(rhs)!!.let { switch ->
		interpreter(
			context,
			typedExpr.expr.plus(op(switch)) of typedExpr.type)
	}

fun Interpreter.switchOrNull(casesScript: Script): TypedExprSwitch? =
	typedExpr.type.onlyChoiceOrNull?.let { choice ->
		zipMapOrNull(choice.lineStack, casesScript.lineStack) { typeLine, caseScriptLine ->
			caseTypedExprOrNull(typeLine, caseScriptLine)
		}
			?.mapOrNull { this }
			?.let { switchOrNull(it) }
	}

fun Interpreter.caseTypedExprOrNull(typeLine: TypeLine, caseScriptLine: ScriptLine): TypedExpr? =
	ifOrNull(caseScriptLine.name == "case") {
		caseScriptLine.rhs.onlyLineOrNull?.let { scriptLine ->
			notNullIf(typeLine.name == scriptLine.name) {
				context.bind(typedExpr).typedExpr(scriptLine.rhs)
			}
		}
	}

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

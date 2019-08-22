package leo13.script

import leo13.*
import leo9.fold
import leo9.reverse

data class Evaluator(
	val types: Types,
	val functions: Functions,
	val typedScript: TypedScript)

val Evaluator.begin
	get() =
		Evaluator(types, functions, script() of type())

fun Evaluator.append(typedScriptLine: TypedScriptLine): Evaluator =
	copy(typedScript = typedScript.plus(typedScriptLine))

fun Evaluator.push(script: Script): Evaluator =
	fold(script.lineStack.reverse, Evaluator::push)

fun Evaluator.push(scriptLine: ScriptLine): Evaluator =
	if (scriptLine.name == "meta") pushMeta(scriptLine.rhs)
	else resolve(scriptLine.name lineTo begin.push(scriptLine.rhs).typedScript)

fun Evaluator.pushMeta(script: Script): Evaluator =
	fold(script.lineStack.reverse, Evaluator::pushMeta)

fun Evaluator.pushMeta(scriptLine: ScriptLine): Evaluator =
	append(scriptLine.name lineTo begin.push(scriptLine.rhs).typedScript)

fun Evaluator.resolve(typedScriptLine: TypedScriptLine): Evaluator =
	when (typedScriptLine.name) {
		"of" -> TODO()
		"gives" -> TODO()
		else -> eval(typedScriptLine)
	}

fun Evaluator.eval(typedScriptLine: TypedScriptLine): Evaluator =
	copy(typedScript =
	types
		.cast(typedScript.plus(typedScriptLine))
		.let { castTypedScript ->
			functions
				.typedExprOrNull(castTypedScript.type)
				?.let { typedExpr ->
					typedExpr.expr.eval(bindings()) of typedExpr.type
				}
				?: castTypedScript
		})

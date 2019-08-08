package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.fold
import leo9.mapOrNull
import leo9.reverse
import leo9.zipMapOrNull

data class Compiler(
	val context: Context,
	val typedExpr: TypedExpr)

fun compile(script: Script) =
	compiler().push(script).typedExpr.script(valueBindings())

fun compiler(context: Context, typedExpr: TypedExpr) =
	Compiler(context, typedExpr)

fun compiler() = compiler(context(), expr() of type())

fun Compiler.push(script: Script): Compiler =
	fold(script.lineStack.reverse) { push(it) }

fun Compiler.push(line: ScriptLine): Compiler =
	when (line.name) {
		"gives" -> pushGives(line.rhs)
		"switch" -> pushSwitch(line.rhs)
		else -> push(line.typedExprLine(context))
	}

fun Compiler.pushGives(rhs: Script): Compiler =
	compiler(
		context.plus(
			function(
				parameter(typedExpr.type),
				context.bind(typedExpr).typedExpr(rhs))),
		expr() of type())

fun Compiler.pushSwitch(rhs: Script): Compiler =
	push(typedSwitchOrNull(rhs)!!)

fun Compiler.typedSwitchOrNull(casesScript: Script): TypedExprSwitch? =
	typedExpr.type.onlyChoiceOrNull?.let { choice ->
		zipMapOrNull(choice.lineStack, casesScript.lineStack) { typeLine, caseScriptLine ->
			caseTypedExprOrNull(typeLine, caseScriptLine)
		}
			?.mapOrNull { this }
			?.let { typedSwitchOrNull(it) }
	}

fun Compiler.push(typedSwitch: TypedExprSwitch): Compiler =
	compiler(context, typedExpr.expr.typedExpr(typedSwitch))

fun Compiler.caseTypedExprOrNull(typeLine: TypeLine, caseScriptLine: ScriptLine): TypedExpr? =
	ifOrNull(caseScriptLine.name == "case") {
		caseScriptLine.rhs.onlyLineOrNull?.let { scriptLine ->
			notNullIf(typeLine.name == scriptLine.name) {
				context.bind(typedExpr).typedExpr(scriptLine.rhs)
			}
		}
	}

fun Compiler.push(line: TypedExprLine): Compiler =
	null
		?: notNullIf(line.name == "contains") { pushContains(line.rhs) }
		?: pushDefineOrNull(line)
		?: pushRaw(line)

fun Compiler.pushContains(typedExpr: TypedExpr): Compiler =
	TODO()

fun Compiler.pushDefineOrNull(line: TypedExprLine) =
	pushDefineRhsOrNull(line) ?: pushLhsDefineOrNull(line)

fun Compiler.pushDefineRhsOrNull(line: TypedExprLine) =
	ifOrNull(typedExpr.type.isEmpty) {
		context.defineOrNull(line.rhs)
	}

fun Compiler.pushLhsDefineOrNull(line: TypedExprLine): Compiler? =
	ifOrNull(line.rhs.type.isEmpty) {
		context.defineOrNull(typedExpr)
	}

fun Context.defineOrNull(typedExpr: TypedExpr): Compiler? =
	typedExpr.type.onlyFunctionTypeOrNull?.let { functionType ->
		compiler(
			plus(
				function(
					parameter(functionType.parameter),
					typedExpr.expr of functionType.result)),
			expr() of type())
	}

fun Compiler.pushRaw(line: TypedExprLine): Compiler =
	compiler(
		context,
		context.typedExpr(typedExpr linkTo line))


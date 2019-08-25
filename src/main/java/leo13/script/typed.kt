package leo13.script

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.Switch
import leo13.script.evaluator.evaluate
import leo9.isEmpty
import leo9.mapFirst
import leo9.mapOnly

data class Typed(val expr: Expr, val type: Type) : AsScriptLine() {
	override val asScriptLine = "typed" lineTo script(expr.asScriptLine, type.asScriptLine)
}

data class TypedLine(val name: String, val rhs: Typed)
data class TypedLink(val lhs: Typed, val typedLine: TypedLine)

fun typed() = expr() of type()
infix fun Expr.of(type: Type) = Typed(this, type)
infix fun String.lineTo(typed: Typed) = TypedLine(this, typed)
fun Typed.linkTo(typedLine: TypedLine) = TypedLink(this, typedLine)
fun Typed.plus(typedLine: TypedLine) = expr.plus(op(typedLine.name lineTo typedLine.rhs.expr)) of type.plus(typedLine.name lineTo typedLine.rhs.type)
fun typed(expr: Expr, type: Type) = Typed(expr, type)

fun Type.castOrNull(typed: Typed): Expr? =
	notNullIf(contains(typed.type)) {
		typed.expr
	}

fun Types.cast(typed: Typed): Typed =
	typeStack.mapFirst {
		castOrNull(typed)?.of(this)
	} ?: typed

fun Types.cast(typedScript: TypedScript): TypedScript =
	cast(typedScript.script.expr of typedScript.type).let { castTypedScript ->
		castTypedScript.expr.evaluate(leo13.script.evaluator.bindings()).scriptOrNull!! of castTypedScript.type
	}

fun Typed.accessOrNull(name: String): Typed? =
	type
		.onlyLineOrNull
		?.rhs
		?.lineStack
		?.mapOnly {
			notNullIf(this.name == name) {
				expr.plus(op(get(name))) of type(this)
			}
		}

val Typed.previousOrNull: Typed?
	get() =
		type.previousOrNull?.let { previousType ->
			typed(expr.plus(op(lhs)), previousType)
		}

val Typed.lineOrNull: Typed?
	get() =
		type.lineOrNull?.let { lineType ->
			typed(expr.plus(op(rhsLine)), lineType)
		}

fun Typed.switchOrNull(switch: Switch): Typed? =
	ifOrNull(type.lineStack.isEmpty) {
		type.choiceOrNull?.let { choice ->
			TODO()
		}
	}

val Script.typed
	get() =
		expr of exactType

val ScriptLine.typed
	get() =
		name lineTo rhs.typed

val TypedLink.ofTypedExprOrNull
	get() =
		ifOrNull(typedLine.name == "of") {
			typedLine.rhs.type.staticScriptOrNull?.typeOrNull?.let { type ->
				type.castOrNull(lhs)?.of(type)
			}
		}

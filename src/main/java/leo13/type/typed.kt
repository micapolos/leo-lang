package leo13.type

import leo.base.notNullIf
import leo13.lhs
import leo13.rhsLine
import leo13.script.*
import leo13.script.Switch
import leo13.value.*
import leo9.mapFirst

data class Typed(val expr: Expr, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "typed"
	override val scriptableBody get() = script(expr.scriptableLine, type.scriptableLine)
}

data class TypedLine(val name: String, val rhs: Typed)
data class TypedLink(val lhs: Typed, val typedLine: TypedLine)

fun typed() = expr() of type()
infix fun Expr.of(type: Type) = Typed(this, type)
infix fun String.lineTo(typed: Typed) = TypedLine(this, typed)
fun Typed.linkTo(typedLine: TypedLine) = TypedLink(this, typedLine)
fun Typed.plus(typedLine: TypedLine) = expr.plus(op(typedLine.name lineTo typedLine.rhs.expr)) of type.plus(typedLine.name lineTo typedLine.rhs.type)
fun typed(expr: Expr, type: Type) = Typed(expr, type)

fun typed(script: Script) = typed(expr(script), script.type)

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
		valueBindings().evaluate(castTypedScript.expr).scriptOrNull!! of castTypedScript.type
	}

fun Typed.accessOrNull(name: String): Typed? =
	type.accessOrNull(name)?.let { accessType ->
		expr.plus(op(get(name))) of accessType
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
	TODO()
//	ifOrNull(type.lineStack.isEmpty) {
//		type.choiceOrNull?.let { choice ->
//			TODO()
//		}
//	}

val Script.typed
	get() =
		expr of type

val ScriptLine.typed
	get() =
		name lineTo rhs.typed

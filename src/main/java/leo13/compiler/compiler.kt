package leo13.compiler

import leo.base.failIfOr
import leo.base.fold
import leo.base.ifOrNull
import leo13.script.*
import leo13.type.*
import leo13.value.*

data class Compiler(
	val context: Context,
	val typed: Typed) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiled"
	override val scriptableBody get() = script(context.scriptableLine, typed.scriptableLine)
}

fun compiler(context: Context, typed: Typed) = Compiler(context, typed)
fun compiler() = compiler(context(), typed())

fun Compiler.unsafePush(script: Script): Compiler =
	fold(script.lineSeq) { unsafePush(it) }

fun Compiler.unsafePush(scriptLine: ScriptLine): Compiler =
	when (scriptLine.name) {
		"apply" -> unsafePushApply(scriptLine.rhs)
		"exists" -> unsafePushExists(scriptLine.rhs)
		"given" -> unsafePushGiven(scriptLine.rhs)
		"gives" -> unsafePushGives(scriptLine.rhs)
		"giving" -> unsafePushGiving(scriptLine.rhs)
		"line" -> unsafePushLine(scriptLine.rhs)
		"meta" -> unsafePushMeta(scriptLine.rhs)
		"of" -> unsafePushOf(scriptLine.rhs)
		"previous" -> unsafePushPrevious(scriptLine.rhs)
		"switch" -> unsafePushSwitch(scriptLine.rhs)
		else -> unsafePushOther(scriptLine)
	}

fun Compiler.unsafePushExists(script: Script): Compiler =
	failIfOr(!script.isEmpty) {
		compiler(context.plus(typed.type.unsafeStaticScript.unsafeType), typed(expr(op(value())), type()))
	}

fun Compiler.unsafePushGives(script: Script): Compiler =
	typed.type.unsafeStaticScript.unsafeType.let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { typedExpr ->
			compiler(
				context.plus(function(parameterType, typedExpr)),
				typed(
					typed.expr.plus(op(value())),
					type()))
		}
	}

fun Compiler.unsafePushGiving(script: Script): Compiler =
	typed.type.unsafeStaticScript.unsafeType.let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { typedExpr ->
			compiler(
				context,
				typed(
					typed.expr.plus(op(value(fn(valueBindings(), typedExpr.expr)))),
					type(arrow(parameterType, typedExpr.type))))
		}
	}

fun Compiler.unsafePushApply(script: Script): Compiler =
	(typed.type as ArrowType).arrow.let { arrow ->
		context.unsafeCompile(script).let { rhsTyped ->
			if (rhsTyped.type != arrow.lhs) error("apply type invalid")
			else compiler(
				context,
				typed(
					typed.expr.plus(op(call(rhsTyped.expr))),
					arrow.rhs))
		}
	}

fun Compiler.unsafePushGiven(script: Script): Compiler =
	failIfOr(!typed.expr.isEmpty || !script.isEmpty) {
		compiler(context, typed(expr(op(given())), context.typeBindings.unsafeType(given())))
	}

fun Compiler.unsafePushOf(script: Script): Compiler =
	script.unsafeType.let { ofScriptType ->
		failIfOr(!ofScriptType.contains(this.typed.type)) {
			compiler(context, typed.expr of ofScriptType)
		}
	}

fun Compiler.unsafePushMeta(script: Script): Compiler =
	fold(script.lineSeq) { scriptLine -> unsafeRhsPush(scriptLine) }

fun Compiler.unsafeRhsPush(scriptLine: ScriptLine): Compiler =
	push(scriptLine.name lineTo context.unsafeCompile(scriptLine.rhs))

fun Compiler.unsafePushPrevious(script: Script): Compiler =
	failIfOr(!script.isEmpty) {
		set(typed.previousOrNull!!)
	}

fun Compiler.unsafePushLine(script: Script): Compiler =
	failIfOr(!script.isEmpty) {
		set(typed.lineOrNull!!)
	}

fun Compiler.unsafePushSwitch(script: Script): Compiler =
	TODO()
//	("switch" lineTo script)
//		.switchOrNull
//		?.let { switch ->
//			(typed.type as ChoiceType).choice.let { choice ->
//				switch
//					.choiceMatchOrNull(choice)
//					?.caseMatchStack
//					?.mapOrNull { caseTypedOrNull(this) }
//					?.typedSwitch
//					?.switchTypedOrNull
//					?.let { switchTyped ->
//						set(
//							typed(
//								typed.expr.plus(op(switchTyped.switch)),
//								switchTyped.type))
//					}
//			}
//		}
//
//fun Compiler.caseTypedOrNull(caseMatch: CaseMatch): CaseTyped? =
//	compiler(context, typed(expr(), caseMatch.case.type))
//		.unsafePush(caseMatch.script)
//		?.typed
//		?.let { rhsTyped ->
//			typed(caseMatch.case.name caseTo rhsTyped.expr, rhsTyped.type)
//		}

fun Compiler.unsafePushOther(typedLine: ScriptLine): Compiler =
	null
		?: pushGetOrNull(typedLine)
		?: unsafeRhsPush(typedLine)

fun Compiler.pushGetOrNull(typedLine: ScriptLine): Compiler? =
	ifOrNull(typedLine.rhs.isEmpty) {
		typed.accessOrNull(typedLine.name)?.let { set(it) }
	}

fun Compiler.push(typedLine: TypedLine): Compiler =
	typed.type.plus(typedLine.name lineTo typedLine.rhs.type).let { type ->
		typed.expr.plus(op(typedLine.name lineTo typedLine.rhs.expr)).let { expr ->
			context.types.containingType(type).let { containingType ->
				context.functions.bodyOrNull(containingType)
					?.let { functionBody ->
						set(
							typed(
								expr.plus(op(call(functionBody.expr))),
								functionBody.type))
					}
					?: set(typed(expr, containingType))
			}
		}
	}

fun Compiler.append(typedLine: TypedLine): Compiler =
	compiler(context, typed.plus(typedLine))

fun Compiler.set(typed: Typed): Compiler =
	compiler(context, typed)

val Script.compile: Typed?
	get() =
		context().unsafeCompile(this)
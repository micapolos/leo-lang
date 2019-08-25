package leo13.compiler

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.script.*
import leo9.mapOrNull

data class Compiled(
	val context: Context,
	val typed: Typed) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "compiled" lineTo script(context.asScriptLine, typed.asScriptLine)
}

// --- constructors

fun compiled(context: Context, typed: Typed) = Compiled(context, typed)
fun compiled() = compiled(context(), typed())

fun Compiled.push(typedLine: TypedLine): Compiled? =
	when (typedLine.name) {
		"exists" -> pushExists(typedLine.rhs)
		"gives" -> pushGives(typedLine.rhs)
		"line" -> pushLine(typedLine.rhs)
		"of" -> pushOf(typedLine.rhs)
		"previous" -> pushPrevious(typedLine.rhs)
		"switch" -> pushSwitch(typedLine.rhs)
		else -> pushOther(typedLine)
	}

fun Compiled.pushExists(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { type ->
		rhsTyped.type.staticScriptOrNull?.let { rhsScript ->
			notNullIf(rhsScript.isEmpty) {
				compiled(context.plus(type), typed())
			}
		}
	}

fun Compiled.pushGives(rhsTyped: Typed): Compiled? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { parameterType ->
		rhsTyped.type.staticScriptOrNull?.let { bodyScript ->
			context.bind(parameterType).typedOrNull(bodyScript)?.let { bodyTyped ->
				compiled(context.plus(function(parameterType, bodyTyped)), typed())
			}
		}
	}

fun Compiled.pushOf(rhsTyped: Typed): Compiled? =
	rhsTyped
		.type
		.staticScriptOrNull
		?.let { ofScript ->
			ofScript.typeOrNull?.let { ofScriptType ->
				notNullIf(ofScriptType.contains(this.typed.type)) {
					compiled(context, typed.expr of ofScriptType)
				}
			}
		}

fun Compiled.pushPrevious(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.previousOrNull?.let { set(it) }
	}

fun Compiled.pushLine(rhsTyped: Typed): Compiled? =
	ifOrNull(rhsTyped.expr.isEmpty) {
		typed.lineOrNull?.let { set(it) }
	}

fun Compiled.pushSwitch(rhsTyped: Typed): Compiled? =
	rhsTyped.type.staticScriptOrNull?.let { rhsStaticScript ->
		pushSwitch(rhsStaticScript)
	}

fun Compiled.pushSwitch(rhsScript: Script): Compiled? =
	("switch" lineTo rhsScript)
		.switchOrNull
		?.let { switch ->
			typed.type.onlyChoiceOrNull?.let { choice ->
				switch
					.choiceMatchOrNull(choice)
					?.eitherMatchStack
					?.mapOrNull { caseTypedOrNull(this) }
					?.typedSwitch
					?.switchTypedOrNull
					?.let { switchTyped ->
						set(
							typed(
								typed.expr.plus(op(switchTyped.switch)),
								switchTyped.type))
					}
			}
		}

fun Compiled.caseTypedOrNull(eitherMatch: EitherMatch): CaseTyped? =
	set(typed(expr(), eitherMatch.either.type))
		.typedOrNull(eitherMatch.script)
		?.let { rhsTyped -> typed(eitherMatch.either.name caseTo rhsTyped.expr, rhsTyped.type) }

fun Compiled.typedOrNull(script: Script): Typed? =
	metable(false, this)
		.head
		.compiler
		.fold(script.tokenSeq) { push(it) }
		.successHeadOrNull
		?.completedCompiledOrNull
		?.typed

fun Compiled.pushOther(typedLine: TypedLine): Compiled =
	null
		?: pushGetOrNull(typedLine)
		?: append(typedLine)

fun Compiled.pushGetOrNull(typedLine: TypedLine): Compiled? =
	ifOrNull(typedLine.rhs.expr.isEmpty) {
		typed.accessOrNull(typedLine.name)?.let { set(it) }
	}

fun Compiled.append(typedLine: TypedLine): Compiled =
	compiled(context, typed.plus(typedLine))

fun Compiled.set(typed: Typed): Compiled =
	copy(typed = typed)

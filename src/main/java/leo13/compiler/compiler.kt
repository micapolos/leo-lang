package leo13.compiler

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.orNull
import leo.base.orNullFold
import leo13.script.*
import leo13.type.*
import leo13.value.*
import leo9.fold
import leo9.mapOrNull
import leo9.reverse

data class Compiler(
	val context: Context,
	val typed: Typed) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiled"
	override val scriptableBody get() = script(context.scriptableLine, typed.scriptableLine)
}

fun compiler(context: Context, typed: Typed) = Compiler(context, typed)
fun compiler() = compiler(context(), typed())

fun Compiler.push(script: Script): Compiler? =
	compiler(context, typed())
		.orNull
		.fold(script.lineStack.reverse) { this?.push(it) }

fun Compiler.push(scriptLine: ScriptLine): Compiler? =
	when (scriptLine.name) {
		"exists" -> pushExists(scriptLine.rhs)
		"given" -> pushGiven(scriptLine.rhs)
		"gives" -> pushGives(scriptLine.rhs)
		"line" -> pushLine(scriptLine.rhs)
		"meta" -> pushMeta(scriptLine.rhs)
		"of" -> pushOf(scriptLine.rhs)
		"previous" -> pushPrevious(scriptLine.rhs)
		"switch" -> pushSwitch(scriptLine.rhs)
		else -> pushOther(scriptLine)
	}

fun Compiler.pushExists(script: Script): Compiler? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { type ->
		notNullIf(script.isEmpty) {
			compiler(context.plus(type), typed())
		}
	}

fun Compiler.pushGives(script: Script): Compiler? =
	typed.type.staticScriptOrNull?.typeOrNull?.let { parameterType ->
		context.bind(parameterType).compile(script)?.let { bodyTyped ->
			compiler(context.plus(function(parameterType, bodyTyped)), typed())
		}
	}

fun Compiler.pushGiven(script: Script): Compiler? =
	ifOrNull(typed.expr.isEmpty) {
		ifOrNull(script.isEmpty) {
			context.typeBindings.typeOrNull(given())?.let { givenType ->
				compiler(context, typed(expr(op(given())), givenType))
			}
		}
	}

fun Compiler.pushOf(script: Script): Compiler? =
	script.typeOrNull?.let { ofScriptType ->
		notNullIf(ofScriptType.contains(this.typed.type)) {
			compiler(context, typed.expr of ofScriptType)
		}
	}

fun Compiler.pushMeta(script: Script): Compiler? =
	orNullFold(script.lineSeq) { scriptLine ->
		rhsPush(scriptLine)
	}

fun Compiler.rhsPush(scriptLine: ScriptLine): Compiler? =
	context
		.compile(scriptLine.rhs)
		?.let { rhsTyped -> append(scriptLine.name lineTo rhsTyped) }

fun Compiler.pushPrevious(script: Script): Compiler? =
	ifOrNull(script.isEmpty) {
		typed.previousOrNull?.let {
			set(it)
		}
	}

fun Compiler.pushLine(script: Script): Compiler? =
	ifOrNull(script.isEmpty) {
		typed.lineOrNull?.let { set(it) }
	}

fun Compiler.pushSwitch(script: Script): Compiler? =
	("switch" lineTo script)
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

fun Compiler.caseTypedOrNull(eitherMatch: EitherMatch): CaseTyped? =
	compiler(context, typed(expr(), eitherMatch.either.type))
		.push(eitherMatch.script)
		?.typed
		?.let { rhsTyped ->
			typed(eitherMatch.either.name caseTo rhsTyped.expr, rhsTyped.type)
		}

fun Compiler.pushOther(typedLine: ScriptLine): Compiler? =
	null
		?: pushGetOrNull(typedLine)
		?: append(typedLine)

fun Compiler.pushGetOrNull(typedLine: ScriptLine): Compiler? =
	ifOrNull(typedLine.rhs.isEmpty) {
		typed.accessOrNull(typedLine.name)?.let { set(it) }
	}

fun Compiler.append(scriptLine: ScriptLine): Compiler? =
	context
		.compile(scriptLine.rhs)
		?.let { typed -> append(scriptLine.name lineTo typed) }

fun Compiler.append(typedLine: TypedLine): Compiler =
	compiler(context, typed.plus(typedLine))

fun Compiler.set(typed: Typed): Compiler =
	compiler(context, typed)

val Script.compile: Typed?
	get() =
		context().compile(this)
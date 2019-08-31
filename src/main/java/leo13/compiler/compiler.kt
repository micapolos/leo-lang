package leo13.compiler

import leo.base.failIfOr
import leo.base.fold
import leo.base.ifOrNull
import leo13.script.*
import leo13.type.*
import leo13.value.*

data class Compiler(
	val context: Context,
	val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiled"
	override val scriptableBody get() = script(context.scriptableLine, compiled.scriptableLine)
}

fun compiler(context: Context, compiled: Compiled) = Compiler(context, compiled)
fun compiler() = compiler(context(), compiled())

fun Compiler.unsafePush(script: Script): Compiler =
	fold(script.lineSeq) { unsafePush(it) }

fun Compiler.unsafePush(scriptLine: ScriptLine): Compiler =
	when (scriptLine.name) {
		"apply" -> unsafePushApply(scriptLine.rhs)
		"exists" -> unsafePushExists(scriptLine.rhs)
		"contains" -> unsafePushContains(scriptLine.rhs)
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
		compiler(
			context.plus(trace(compiled.trace.type.unsafeStaticScript.unsafeType)),
			compiled())
	}

fun Compiler.unsafePushContains(script: Script): Compiler =
	script.unsafeType.let { type ->
		compiler(
			context()
				.plus(type.trace)
				.plus(
					function(
						trace(compiled.trace.type.unsafeStaticScript.unsafeType),
						compiled(script))),
			compiled())
	}

fun Compiler.unsafePushGives(script: Script): Compiler =
	compiled.trace.type.unsafeStaticScript.unsafeType.let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { compiled ->
			compiler(
				context.plus(function(trace(parameterType), compiled)),
				compiled())
		}
	}

fun Compiler.unsafePushGiving(script: Script): Compiler =
	compiled.trace.type.unsafeStaticScript.unsafeType.let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { scriptCompiled ->
			compiler(
				context,
				compiled(
					expr(op(value(fn(valueBindings(), scriptCompiled.expr)))),
					// TODO: Traces!!!
					trace(type(arrow(parameterType, scriptCompiled.trace.type)))))
		}
	}

fun Compiler.unsafePushApply(script: Script): Compiler =
	compiled.trace.type.unsafeArrow.let { arrow ->
		context.unsafeCompile(script).let { scriptCompiled ->
			// TODO: Traces!!!
			if (scriptCompiled.trace.type != arrow.lhs) error("apply type invalid")
			else compiler(
				context,
				compiled(
					compiled.expr.plus(op(call(scriptCompiled.expr))),
					trace(arrow.rhs)))
		}
	}

fun Compiler.unsafePushGiven(script: Script): Compiler =
	failIfOr(!compiled.expr.isEmpty || !script.isEmpty) {
		compiler(
			context,
			compiled(
				expr(op(given())),
				trace(context.typeBindings.unsafeType(given()))))
	}

fun Compiler.unsafePushOf(script: Script): Compiler =
	script.unsafeType.let { ofScriptType ->
		failIfOr(!ofScriptType.contains(compiled.trace.type)) {
			compiler(
				context,
				compiled(
					compiled.expr,
					trace(ofScriptType)))
		}
	}

fun Compiler.unsafePushMeta(script: Script): Compiler =
	fold(script.lineSeq) { scriptLine -> unsafeRhsPush(scriptLine) }

fun Compiler.unsafeRhsPush(scriptLine: ScriptLine): Compiler =
	push(scriptLine.name lineTo context.unsafeCompile(scriptLine.rhs))

fun Compiler.unsafePushPrevious(script: Script): Compiler =
	failIfOr(!script.isEmpty) {
		compiler(
			context,
			compiled.lhsOrNull!!)
	}

fun Compiler.unsafePushLine(script: Script): Compiler =
	failIfOr(!script.isEmpty) {
		compiler(
			context,
			compiled.lineOrNull!!)
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
		compiled
			.accessOrNull(typedLine.name)
			?.let { compiledAccess ->
				compiler(context, compiledAccess)
			}
	}

fun Compiler.push(compiledLine: CompiledLine): Compiler =
	compiled.plus(compiledLine).let { plusCompiled ->
		context.traces.resolve(plusCompiled.trace).let { resolvedTrace ->
			context.functions.compiledOrNull(resolvedTrace)
				?.let { functionBody ->
					compiler(
						context,
						compiled(
							expr(
								op(value(fn(valueBindings(), functionBody.expr))),
								op(call(plusCompiled.expr))),
							functionBody.trace))
				}
				?: compiler(
					context,
					compiled(
						plusCompiled.expr,
						resolvedTrace))
		}
	}

fun Compiler.append(compiledLine: CompiledLine): Compiler =
	compiler(
		context,
		compiled.plus(compiledLine))

val Script.compile: Compiled?
	get() =
		context().unsafeCompile(this)
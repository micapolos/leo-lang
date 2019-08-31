package leo13.compiler

import leo.base.failIfOr
import leo.base.fold
import leo.base.ifOrNull
import leo13.script.*
import leo13.script.Switch
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
			context.plus(type(compiled.type.pattern.unsafeStaticScript.unsafePattern)),
			compiled())
	}

fun Compiler.unsafePushContains(script: Script): Compiler =
	script.unsafePattern.let { type ->
		compiler(
			context()
				.plus(type.type)
				.plus(
					function(
						type(compiled.type.pattern.unsafeStaticScript.unsafePattern),
						compiled(script))),
			compiled())
	}

fun Compiler.unsafePushGives(script: Script): Compiler =
	type(compiled.type.pattern.unsafeStaticScript.unsafePattern).let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { compiled ->
			compiler(
				context.plus(function(parameterType, compiled)),
				compiled())
		}
	}

fun Compiler.unsafePushGiving(script: Script): Compiler =
	type(compiled.type.pattern.unsafeStaticScript.unsafePattern).let { parameterType ->
		context.bind(parameterType).unsafeCompile(script).let { scriptCompiled ->
			compiler(
				context,
				compiled(
					expr(value(fn(valueBindings(), scriptCompiled.expr))),
					// TODO: Traces!!!
					type(pattern(arrow(parameterType, scriptCompiled.type)))))
		}
	}

fun Compiler.unsafePushApply(script: Script): Compiler =
	compiled.type.pattern.unsafeArrow.let { arrow ->
		context.unsafeCompile(script).let { scriptCompiled ->
			// TODO: Traces!!!
			if (scriptCompiled.type != arrow.lhs) error("apply type invalid")
			else compiler(
				context,
				compiled(
					compiled.expr.plus(op(call(scriptCompiled.expr))),
					arrow.rhs))
		}
	}

fun Compiler.unsafePushGiven(script: Script): Compiler =
	failIfOr(!compiled.expr.isEmpty || !script.isEmpty) {
		compiler(
			context,
			compiled(
				expr(given()),
				context.typeBindings.unsafeType(given())))
	}

fun Compiler.unsafePushOf(script: Script): Compiler =
	script.unsafePattern.let { ofScriptType ->
		failIfOr(!ofScriptType.contains(compiled.type.pattern)) {
			compiler(
				context,
				compiled(
					compiled.expr,
					type(ofScriptType)))
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
	unsafePush(script.unsafeSwitch)

fun Compiler.unsafePush(switch: Switch): Compiler =
	TODO()

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
		context.types.resolve(plusCompiled.type).let { resolvedTrace ->
			context.functions.compiledOrNull(resolvedTrace)
				?.let { functionBody ->
					compiler(
						context,
						compiled(
							expr(value(fn(valueBindings(), functionBody.expr)))
								.plus(op(call(plusCompiled.expr))),
							functionBody.type))
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
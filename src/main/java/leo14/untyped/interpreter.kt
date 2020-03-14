package leo14.untyped

import leo14.*

data class Interpreter(
	val context: Context,
	val script: Script)

fun Context.interpreter() =
	Interpreter(this, script())

fun interpreter() =
	context().interpreter()

fun Interpreter.interpret(scriptLine: ScriptLine): Interpreter {
	val scriptLink = ScriptLink(script, scriptLine)
	val resolvedOrNull = context.resolve(scriptLink)
	return if (resolvedOrNull != null) set(resolvedOrNull)
	else resolve(scriptLink)
}

fun Interpreter.resolve(scriptLink: ScriptLink): Interpreter =
	null
		?: resolveDoes(scriptLink)
		?: resolveGives(scriptLink)
		?: replace(scriptLink)

fun Interpreter.resolveDoes(scriptLink: ScriptLink): Interpreter? =
	scriptLink.match("does") { lhs, rhs ->
		lhs.matchLink { lhs ->
			copy(context = context.push(Rule(Pattern(lhs), Body(rhs, context))))
		}
	}

fun Interpreter.resolveGives(scriptLink: ScriptLink): Interpreter? =
	scriptLink.match("gives") { lhs, rhs ->
		lhs.matchLink { lhs ->
			Interpreter(context.push(Rule(Pattern(lhs), Body(rhs, null))), script())
		}
	}

fun Interpreter.replace(scriptLink: ScriptLink): Interpreter =
	set(LinkScript(scriptLink))

fun Interpreter.set(script: Script): Interpreter =
	copy(script = script)

val Interpreter.clear
	get() =
		set(script())

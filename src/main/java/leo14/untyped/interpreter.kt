package leo14.untyped

import leo14.LinkScript
import leo14.Script
import leo14.ScriptLine
import leo14.ScriptLink

data class Interpreter(
	val context: Context,
	val script: Script)

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
			copy(context = context.push(Rule(Pattern(lhs), Body(rhs, null))))
		}
	}

fun Interpreter.replace(scriptLink: ScriptLink): Interpreter =
	set(LinkScript(scriptLink))

fun Interpreter.set(script: Script): Interpreter =
	copy(script = script)

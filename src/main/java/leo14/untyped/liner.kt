package leo14.untyped

import leo14.*

data class Liner(
	val context: Context,
	val script: Script)

fun Context.liner() =
	Liner(this, script())

fun liner() =
	context().liner()

fun Liner.append(scriptLine: ScriptLine): Liner {
	val scriptLink = ScriptLink(script, scriptLine)
	val resolvedOrNull = context.resolve(scriptLink)
	return if (resolvedOrNull != null) set(resolvedOrNull)
	else resolve(scriptLink)
}

fun Liner.resolve(scriptLink: ScriptLink): Liner =
	null
		?: resolveDoes(scriptLink)
		?: resolveGives(scriptLink)
		?: resolveDo(scriptLink)
		?: resolveCompile(scriptLink)
		?: replace(scriptLink)

fun Liner.resolveDoes(scriptLink: ScriptLink): Liner? =
	scriptLink.match("does") { lhs, rhs ->
		lhs.matchLink { lhs ->
			Liner(context.push(Rule(Pattern(lhs), body(context.function(rhs)))), script())
		}
	}

fun Liner.resolveGives(scriptLink: ScriptLink): Liner? =
	scriptLink.match("gives") { lhs, rhs ->
		lhs.matchLink { lhs ->
			Liner(context.push(Rule(Pattern(lhs), body(rhs))), script())
		}
	}

fun Liner.resolveDo(scriptLink: ScriptLink) =
	scriptLink.match("do") { lhs, rhs ->
		rhs.matchEmpty {
			Liner(context, context.eval(script))
		}
	}

fun Liner.resolveCompile(scriptLink: ScriptLink) =
	scriptLink.match("compile") { lhs, rhs ->
		rhs.matchEmpty {
			set(script()).tokenizer().append(script).liner
		}
	}

fun Liner.replace(scriptLink: ScriptLink): Liner =
	set(LinkScript(scriptLink))

fun Liner.set(script: Script): Liner =
	copy(script = script)

val Liner.clear
	get() =
		set(script())

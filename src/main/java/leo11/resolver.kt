package leo11

import leo.base.empty
import leo.base.notNullIf
import leo9.fold

data class Resolver(
	val function: Function,
	val givenScript: Script,
	val evaluatedScript: Script)

fun Resolver.resolve(script: Script): Resolver =
	fold(script.lineStack) { resolve(it) }

fun Resolver.resolve(line: ScriptLine): Resolver =
	evaluatedScript.plusLink(line).let { link ->
		when (link.name) {
			"gives" ->
				Resolver(
					function.plus(link.args.lhs.pattern ruleTo body(function, link.args.rhs)),
					givenScript,
					script(empty))
			"given" ->
				notNullIf(link.args is EmptyScriptArgs) {
					Resolver(
						function,
						givenScript,
						givenScript)
				}
			else -> null
		} ?: function.resolve(link)
	}

fun Function.resolve(link: ScriptLink): Resolver =
	link.evalGet ?: resolve(link)

val ScriptLink.evalGet: Resolver? get() = TODO()

fun Function.resolve(script: Script): Script = TODO()

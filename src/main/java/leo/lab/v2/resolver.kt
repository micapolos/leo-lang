package leo.lab.v2

import leo.Word
import leo.base.fold
import leo.base.orNull

data class Resolver(
	val match: Match,
	val trace: Trace)

val Pattern.resolver: Resolver
	get() =
		Resolver(match(this), trace)

fun Resolver.invoke(command: Command): Resolver? =
	when (command) {
		is WordBeginCommand -> begin(command.word)
		is EndCommand -> end
	}

fun Resolver.begin(word: Word): Resolver? =
	match.patternOrNull?.let { pattern ->
		when (pattern) {
			is SwitchPattern -> pattern.get(word)?.let { beganPattern ->
				Resolver(match(beganPattern), trace.childTrace(pattern))
			}
			is RecursionPattern -> pattern.recursion.apply(trace)?.let { recursedTrace ->
				Resolver(match(recursedTrace.pattern), recursedTrace)
			}
		}
	}

val Resolver.end: Resolver?
	get() =
		match.patternOrNull?.let { pattern ->
			trace.parentTraceOrNull?.let { parentTrace ->
				when (pattern) {
					is SwitchPattern -> pattern.endMatchOrNull?.let { endMatch ->
						Resolver(endMatch, parentTrace)
					}
					is RecursionPattern -> pattern.recursion.apply(parentTrace)?.let { recursedTrace ->
						Resolver(match(recursedTrace.pattern), recursedTrace)
					}
				}
			}
		}

fun Resolver.invokeOrThis(script: Script?): Resolver? =
	if (script == null) this
	else invoke(script)

fun Resolver.invoke(script: Script): Resolver? =
	orNull.fold(script.commandStream) { this?.invoke(it) }

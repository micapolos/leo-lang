package leo.lab.v2

import leo.Word
import leo.base.fold
import leo.base.orNull

data class Resolver(
	val match: Match,
	val traceLinkOrNull: TraceLink?)

val Function.resolver: Resolver
	get() =
		Resolver(match(this), null)

fun Resolver.invoke(command: Command): Resolver? =
	when (command) {
		is WordBeginCommand -> begin(command.word)
		is EndCommand -> end
	}

fun Resolver.begin(word: Word): Resolver? =
	match.functionOrNull?.let { pattern ->
		when (pattern) {
			is SwitchFunction -> pattern.get(word)?.let { childPattern ->
				Resolver(match(childPattern), traceLinkOrNull.plus(pattern).plus(parent.jump)).resolveRecursion
			}
			is RecursionFunction -> null
		}
	}

val Resolver.end: Resolver?
	get() =
		match.functionOrNull?.let { pattern ->
			traceLinkOrNull?.let { traceLink ->
				traceLink.parentTraceOrNull?.let { parentTrace ->
					when (pattern) {
						is SwitchFunction -> pattern.endMatchOrNull?.let { endMatch ->
							Resolver(endMatch, parentTrace.plus(sibling.jump)).resolveRecursion
						}
						is RecursionFunction -> null
					}
				}
			}
		}

val Resolver.resolveRecursion: Resolver?
	get() =
		when (match) {
			is BodyMatch -> this
			is FunctionMatch -> when (match.function) {
				is SwitchFunction -> this
				is RecursionFunction -> match.function.recursion.orNullApply(traceLinkOrNull.plus(match.function))?.let { appliedTrace ->
					Resolver(match(appliedTrace.function), appliedTrace.traceLinkOrNull)
				}
			}
		}

fun Resolver.invokeOrThis(script: Script?): Resolver? =
	if (script == null) this
	else invoke(script)

fun Resolver.invoke(script: Script): Resolver? =
	orNull.fold(script.commandStream) { this?.invoke(it) }

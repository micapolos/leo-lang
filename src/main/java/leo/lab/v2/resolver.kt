package leo.lab.v2

import leo.Word
import leo.base.fold
import leo.base.orNull

data class Resolver(
	val match: Match,
	val traceLinkOrNull: TraceLink?)

val Pattern.resolver: Resolver
	get() =
		Resolver(match(this), null)

fun Resolver.invoke(command: Command): Resolver? =
	when (command) {
		is WordBeginCommand -> begin(command.word)
		is EndCommand -> end
	}

fun Resolver.begin(word: Word): Resolver? =
	match.patternOrNull?.let { pattern ->
		when (pattern) {
			is SwitchPattern -> pattern.get(word)?.let { childPattern ->
				Resolver(match(childPattern), traceLinkOrNull.plus(pattern).plus(parent.jump)).resolveRecursion
			}
			is RecursionPattern -> null
		}
	}

val Resolver.end: Resolver?
	get() =
		match.patternOrNull?.let { pattern ->
			traceLinkOrNull?.let { traceLink ->
				traceLink.parentTraceOrNull?.let { parentTrace ->
					when (pattern) {
						is SwitchPattern -> pattern.endMatchOrNull?.let { endMatch ->
							Resolver(endMatch, parentTrace.plus(sibling.jump)).resolveRecursion
						}
						is RecursionPattern -> null
					}
				}
			}
		}

val Resolver.resolveRecursion: Resolver?
	get() =
		when (match) {
			is TemplateMatch -> this
			is PatternMatch -> when (match.pattern) {
				is SwitchPattern -> this
				is RecursionPattern -> match.pattern.recursion.orNullApply(traceLinkOrNull.plus(match.pattern))?.let { appliedTrace ->
					Resolver(match(appliedTrace.pattern), appliedTrace.traceLinkOrNull)
				}
			}
		}

fun Resolver.invokeOrThis(script: Script?): Resolver? =
	if (script == null) this
	else invoke(script)

fun Resolver.invoke(script: Script): Resolver? =
	orNull.fold(script.commandStream) { this?.invoke(it) }

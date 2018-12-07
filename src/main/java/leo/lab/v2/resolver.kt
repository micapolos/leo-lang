package leo.lab.v2

import leo.Word
import leo.base.fold
import leo.base.orNull

data class Resolver(
	val match: Match,
	val backTraceOrNull: BackTrace?)

val Match.resolver: Resolver
	get() =
		Resolver(this, null)

fun Resolver.invoke(command: Command): Resolver? =
	when (command) {
		is WordBeginCommand -> begin(command.word)
		is EndCommand -> end
	}

fun Resolver.begin(word: Word): Resolver? =
	match.patternOrNull?.let { pattern ->
		pattern.get(word)?.let { beganPattern ->
			Resolver(PatternMatch(beganPattern), backTraceOrNull.push(pattern))
		}
	}

val Resolver.end: Resolver?
	get() =
		backTraceOrNull?.let { backTrace ->
			match.patternOrNull?.let { pattern ->
				pattern.endResolutionOrNull?.let { resolution ->
					when (resolution) {
						is MatchResolution -> Resolver(resolution.match, backTrace.back)
						is RecursionResolution -> resolution.recursion.apply(backTraceOrNull)?.let { recursedBackTrace ->
							Resolver(PatternMatch(recursedBackTrace.pattern), recursedBackTrace.back)
						}
					}
				}
			}
		}

fun Resolver.invokeOrThis(script: Script?): Resolver? =
	if (script == null) this
	else invoke(script)

fun Resolver.invoke(script: Script): Resolver? =
	orNull.fold(script.commandStream) { this?.invoke(it) }

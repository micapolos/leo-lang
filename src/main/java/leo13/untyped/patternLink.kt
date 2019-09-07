package leo13.untyped

import leo13.script.*

data class PatternLink(val lhs: Pattern, val line: PatternLine)

fun link(lhs: Pattern, line: PatternLine) = PatternLink(lhs, line)

fun PatternLink.matches(script: Script): Boolean =
	script.linkOrNull?.let { matches(it) } ?: false

fun PatternLink.matches(scriptLink: ScriptLink): Boolean =
	lhs.matches(scriptLink.lhs) && line.matches(scriptLink.line)

const val patternLinkName = "link"

val patternLinkReader: Reader<PatternLink> =
	reader(patternLinkName) {
		unsafeLink
			.run {
				link(
					patternReader.unsafeBodyValue(lhs),
					patternLineReader.unsafeBodyValue(line))
			}
	}

val patternLinkWriter: Writer<PatternLink> =
	writer(patternLinkName) {
		patternWriter
			.bodyScript(lhs)
			.plus(patternLineWriter.bodyScript(line))
	}

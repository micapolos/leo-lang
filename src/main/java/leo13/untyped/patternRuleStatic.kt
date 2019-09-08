package leo13.untyped

import leo13.script.*

data class PatternRuleStatic(val lhs: Pattern, val line: PatternLine)

fun rule(lhs: Pattern, line: PatternLine) = PatternRuleStatic(lhs, line)

fun PatternRuleStatic.matches(script: Script): Boolean =
	script.linkOrNull?.let { matches(it) } ?: false

fun PatternRuleStatic.matches(scriptLink: ScriptLink): Boolean =
	lhs.matches(scriptLink.lhs) && line.matches(scriptLink.line)

const val patternRuleStaticName = "static"

val patternRuleStaticReader: Reader<PatternRuleStatic> =
	reader(patternRuleStaticName) {
		unsafeLink
			.run {
				rule(
					patternReader.unsafeBodyValue(lhs),
					patternLineReader.unsafeBodyValue(line))
			}
	}

val patternRuleStaticWriter: Writer<PatternRuleStatic> =
	writer(patternRuleStaticName) {
		patternWriter
			.bodyScript(lhs)
			.plus(patternLineWriter.bodyScript(line))
	}

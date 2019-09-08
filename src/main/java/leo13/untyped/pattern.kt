package leo13.untyped

import leo.base.fold
import leo.base.orIfNull
import leo13.script.*

data class Pattern(val ruleOrNull: PatternRule?)

fun pattern(ruleOrNull: PatternRule?) = Pattern(ruleOrNull)

fun pattern(vararg lines: PatternLine) =
	pattern(null).fold(lines) { plus(it) }

fun pattern(dynamic: PatternRuleDynamic, vararg lines: PatternLine) =
	pattern(rule(dynamic)).fold(lines) { plus(it) }

fun pattern(choice: Choice, vararg lines: PatternLine) =
	pattern(dynamic(choice), *lines)

fun pattern(script: ObjectScript, vararg lines: PatternLine) =
	pattern(dynamic(script), *lines)

fun pattern(name: String) = pattern(name lineTo pattern())

fun Pattern.plus(line: PatternLine) =
	if (ruleOrNull == null) pattern(rule(line))
	else pattern(ruleOrNull.plus(line))

fun pattern(script: Script): Pattern =
	patternReader.unsafeBodyValue(script)

fun Pattern.matches(script: Script): Boolean =
	if (ruleOrNull == null) script.isEmpty
	else ruleOrNull.matches(script)

val patternName: String = "pattern"

val patternReader: Reader<Pattern> =
	reader(patternName) {
		if (isEmpty) pattern(null)
		else pattern(patternRuleReader.unsafeBodyValue(this))
	}

val patternWriter: Writer<Pattern> =
	writer(patternName) {
		ruleOrNull
			?.run { patternRuleWriter.bodyScript(this) }
			.orIfNull { script() }
	}

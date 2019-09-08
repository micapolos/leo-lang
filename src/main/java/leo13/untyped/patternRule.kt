package leo13.untyped

import leo13.script.*
import leo9.contains

sealed class PatternRule

data class DynamicPatternRule(val dynamic: PatternRuleDynamic) : PatternRule()
data class StaticPatternRule(val static: PatternRuleStatic) : PatternRule()

fun rule(dynamic: PatternRuleDynamic): PatternRule = DynamicPatternRule(dynamic)
fun rule(static: PatternRuleStatic): PatternRule = StaticPatternRule(static)

fun rule(line: PatternLine): PatternRule =
	rule(rule(pattern(null), line))

fun PatternRule.plus(line: PatternLine): PatternRule =
	rule(rule(pattern(this), line))

fun PatternRule.matches(script: Script): Boolean =
	when (this) {
		is DynamicPatternRule -> dynamic.matches(script)
		is StaticPatternRule -> static.matches(script)
	}

val patternRuleName = "rule"

val patternRuleReader: Reader<PatternRule> =
	reader(patternRuleName) {
		unsafeLink.run {
			when {
				lhs.isEmpty && dynamicPatternRuleNames.contains(line.name) -> rule(patternRuleDynamicReader.unsafeBodyValue(line))
				else -> rule(patternRuleStaticReader.unsafeBodyValue(script))
			}
		}
	}

val patternRuleWriter: Writer<PatternRule> =
	writer(patternRuleName) {
		when (this) {
			is DynamicPatternRule -> patternRuleDynamicWriter.bodyScript(dynamic)
			is StaticPatternRule -> patternRuleStaticWriter.bodyScript(static)
		}
	}

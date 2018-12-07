package leo.lab.v2

sealed class Match

data class TemplateMatch(
	val template: Template) : Match()

data class PatternMatch(
	val pattern: Pattern) : Match()

fun match(template: Template): Match =
	TemplateMatch(template)

fun match(pattern: Pattern): Match =
	PatternMatch(pattern)

val Match.templateOrNull: Template?
	get() =
		(this as? TemplateMatch)?.template

val Match.patternOrNull: Pattern?
	get() =
		(this as? PatternMatch)?.pattern

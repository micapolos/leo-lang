package leo13.pattern

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class PatternContext(val given: PatternGiven) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"context" lineTo script(given.scriptingLine)

	fun give(pattern: Pattern) =
		PatternContext(given.give(pattern))

//	fun resolve(pattern: Pattern): Pattern =
//		pattern.applyOrNull?.let { apply ->
//			give(apply.rhsPattern).evaluate(apply.rhsPattern)
//		} ?: pattern
//
//	fun evaluate(pattern: Pattern): Pattern =
//		when (pattern) {
//			is EmptyPattern -> pattern(evaluate(pattern.empty))
//			is ChoicePattern -> pattern(evaluate(pattern.choice))
//			is ArrowPattern -> pattern(evaluate(pattern.arrow))
//			is FunctionPattern -> pattern(evaluate(pattern.function))
//			is GivenPattern -> evaluate(pattern.given)
//			is ApplyPattern -> evaluate(pattern.apply)
//			is LinkPattern -> pattern(evaluate(pattern.link))
//		}
//
//	fun evaluate(empty: Empty) = empty
//	fun evaluate(line: PatternLine) = line.name lineTo evaluate(line.rhs)
//	fun evaluate(choice: Choice) = choice.eitherStack.map { evaluate(this) }.choice
//	fun evaluate(either: Either) = either.name eitherTo evaluate(either.rhs)
//	fun evaluate(arrow: PatternArrow) = evaluate(arrow.lhs) arrowTo evaluate(arrow.rhs)
//	fun evaluate(function: PatternFunction) = function // TODO: Should I evaluate body expression?
//	fun evaluate(given: Given) = this.given.pattern
//	fun evaluate(apply: PatternApply) = give(evaluate(apply.rhsPattern)).evaluate(apply.lhsPattern)
//	fun evaluate(link: PatternLink) = evaluate(link.lhs) linkTo evaluate(link.line)
}

fun patternContext() = PatternContext(patternGiven())


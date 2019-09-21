package leo13.pattern

import leo13.Given
import leo13.script.ScriptLine
import leo13.scriptLine
import leo13.value.FunctionValueItem
import leo13.value.LineValueItem
import leo13.value.ValueItem

sealed class PatternItem

data class ChoicePatternItem(val choice: Choice): PatternItem()
data class ArrowPatternItem(val arrow: PatternArrow): PatternItem()
data class FunctionPatternItem(val function: PatternFunction) : PatternItem()
data class GivenPatternItem(val given: Given) : PatternItem()
data class ApplyPatternItem(val apply: PatternApply) : PatternItem()

fun item(choice: Choice): PatternItem = ChoicePatternItem(choice)
fun item(arrow: PatternArrow): PatternItem = ArrowPatternItem(arrow)
fun item(function: PatternFunction): PatternItem = FunctionPatternItem(function)
fun item(given: Given): PatternItem = GivenPatternItem(given)
fun item(apply: PatternApply): PatternItem = ApplyPatternItem(apply)

val PatternItem.choiceOrNull get() = (this as? ChoicePatternItem)?.choice
val PatternItem.arrowOrNull get() = (this as? ArrowPatternItem)?.arrow
val PatternItem.functionOrNull get() = (this as? FunctionPatternItem)?.function
val PatternItem.givenOrNull get() = (this as? GivenPatternItem)?.given
val PatternItem.applyOrNull get() = (this as? ApplyPatternItem)?.apply
val PatternItem.lineOrNull get() = choiceOrNull?.onlyEitherOrNull?.patternLine

fun PatternItem.matches(valueItem: ValueItem): Boolean =
	when (this) {
		is ChoicePatternItem -> valueItem is LineValueItem && choice.matches(valueItem.line)
		is ArrowPatternItem -> valueItem is FunctionValueItem && arrow.matches(valueItem.function)
		else -> TODO()
	}

fun PatternItem.contains(item: PatternItem, context: PatternContext = patternContext()): Boolean =
	when (this) {
		is ChoicePatternItem -> choice.contains(item)
		is ArrowPatternItem -> item is ArrowPatternItem && arrow.contains(item.arrow)
		is FunctionPatternItem -> false // TODO: Or maybe we can do something about it?
		is GivenPatternItem -> context.given.pattern.linkOrNull?.item?.lineOrNull?.rhs
			?.contains(pattern(item), context) ?: false // TODO: This is wrong, needs Pattern refactoring
		is ApplyPatternItem -> apply.lhsPattern.linkOrNull?.item?.functionOrNull?.let { function ->
			function.pattern.contains(pattern(item), patternContext().give(apply.rhsPattern))
		} ?: false
	}

fun PatternItem.replaceLineOrNull(line: PatternLine): PatternItem? =
	choiceOrNull?.replaceLineOrNull(line)?.let { item(it) }

val PatternItem.scriptLine: ScriptLine
	get() =
		when (this) {
			is ChoicePatternItem -> choice.scriptLine
			is ArrowPatternItem -> arrow.scriptingLine
			is FunctionPatternItem -> function.scriptingLine
			is GivenPatternItem -> given.scriptLine
			is ApplyPatternItem -> apply.scriptingLine
		}

val PatternItem.staticScriptLineOrNull: ScriptLine?
	get() =
		choiceOrNull?.onlyEitherOrNull?.patternLine?.staticScriptLineOrNull

fun PatternItem.leafPlusOrNull(pattern: Pattern): PatternItem? =
	choiceOrNull?.leafPlusOrNull(pattern)?.let { item(it) }

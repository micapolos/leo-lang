package leo13.untyped

import leo13.untyped.value.FunctionValueItem
import leo13.untyped.value.LineValueItem
import leo13.untyped.value.ValueItem

sealed class PatternItem

data class ChoicePatternItem(val choice: Choice): PatternItem()
data class ArrowPatternItem(val arrow: PatternArrow): PatternItem()

fun item(choice: Choice): PatternItem = ChoicePatternItem(choice)
fun item(arrow: PatternArrow): PatternItem = ArrowPatternItem(arrow)

val PatternItem.choiceOrNull get() = (this as? ChoicePatternItem)?.choice
val PatternItem.arrowOrNull get() = (this as? ArrowPatternItem)?.arrow

fun PatternItem.matches(valueItem: ValueItem): Boolean =
	when (this) {
		is ChoicePatternItem -> valueItem is LineValueItem && choice.matches(valueItem.line)
		is ArrowPatternItem -> valueItem is FunctionValueItem && arrow.matches(valueItem.function)
	}
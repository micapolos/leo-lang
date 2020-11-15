package leo21.evaluator

import leo14.lambda.value.Value
import leo14.lambda.value.switch
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Type
import leo21.type.choice
import leo21.type.linkOrNull

data class ChoiceEvaluated(val value: Value<Prim>?, val choice: Choice)

infix fun Value<Prim>.of(choice: Choice) = ChoiceEvaluated(this, choice)
val emptyChoiceEvaluated = ChoiceEvaluated(null, choice())

fun <R> ChoiceEvaluated.switch(
	firstFn: (ChoiceEvaluated) -> R,
	secondFn: (LineEvaluated) -> R
): R =
	choice.linkOrNull!!.let { choiceLink ->
		value!!.switch(
			{ tailValue ->
				firstFn(tailValue of choiceLink.tail)
			},
			{ headValue ->
				secondFn(headValue of choiceLink.head)
			})
	}

fun ChoiceEvaluated.plusNotChosen(type: Type): ChoiceEvaluated =
	TODO()

fun ChoiceEvaluated.plusChosen(lineEvaluated: LineEvaluated): ChoiceEvaluated =
	TODO()

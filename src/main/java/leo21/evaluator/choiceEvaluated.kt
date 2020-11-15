package leo21.evaluator

import leo14.lambda.value.Value
import leo14.lambda.value.eitherFirst
import leo14.lambda.value.eitherSecond
import leo14.lambda.value.switch
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.linkOrNull
import leo21.type.plus

data class ChoiceEvaluated(val valueOrNull: Value<Prim>?, val choice: Choice)

infix fun Value<Prim>?.of(choice: Choice) = ChoiceEvaluated(this, choice)
val emptyChoiceEvaluated = ChoiceEvaluated(null, choice())

fun <R> ChoiceEvaluated.switch(
	firstFn: (ChoiceEvaluated) -> R,
	secondFn: (LineEvaluated) -> R
): R =
	choice.linkOrNull!!.let { choiceLink ->
		valueOrNull!!.switch(
			{ tailValue ->
				firstFn(tailValue of choiceLink.tail)
			},
			{ headValue ->
				secondFn(headValue of choiceLink.head)
			})
	}

fun ChoiceEvaluated.plusNotChosen(line: Line): ChoiceEvaluated =
	valueOrNull?.eitherFirst of choice.plus(line)

fun ChoiceEvaluated.plusChosen(lineEvaluated: LineEvaluated): ChoiceEvaluated =
	lineEvaluated.value.eitherSecond of choice.plus(lineEvaluated.line)

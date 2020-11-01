package leo21.typed

import leo14.lambda.Term
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceTyped(
	val valueTermOrNull: Term<Value>?,
	val choice: Choice
)

val emptyChoiceTyped = ChoiceTyped(null, choice())

fun ChoiceTyped.plusChosen(typed: LineTyped): ChoiceTyped =
	if (valueTermOrNull != null) error("already chosen")
	else ChoiceTyped(typed.valueTerm.eitherFirst, choice.plus(typed.line))

fun ChoiceTyped.plusNotChosen(line: Line): ChoiceTyped =
	ChoiceTyped(valueTermOrNull?.eitherSecond, choice.plus(line))

val ChoiceTyped.typed: Typed
	get() =
		if (valueTermOrNull == null) error("no choice")
		else Typed(valueTermOrNull, type(choice))

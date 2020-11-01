package leo21.typed

import leo14.lambda.Term
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Field
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceTyped(
	val valueTermOrNull: Term<Value>?,
	val choice: Choice
)

val emptyChoiceTyped = ChoiceTyped(null, choice())

fun ChoiceTyped.plusChosen(typed: FieldTyped): ChoiceTyped =
	if (valueTermOrNull != null) error("already chosen")
	else ChoiceTyped(typed.valueTerm.eitherFirst, choice.plus(typed.field))

fun ChoiceTyped.plusNotChosen(field: Field): ChoiceTyped =
	ChoiceTyped(valueTermOrNull?.eitherSecond, choice.plus(field))

val ChoiceTyped.typed: Typed
	get() =
		if (valueTermOrNull == null) error("no choice")
		else Typed(valueTermOrNull, type(choice))

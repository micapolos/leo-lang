package leo21.typed

import leo13.linkOrNull
import leo14.lambda.Term
import leo14.lambda.either
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceTyped(
	val termOrNull: Term<Prim>?,
	val choice: Choice
)

val emptyChoiceTyped = ChoiceTyped(null, choice())

fun ChoiceTyped.plusChosen(typed: LineTyped): ChoiceTyped =
	if (termOrNull != null) error("already chosen")
	else ChoiceTyped(typed.term.eitherFirst, choice.plus(typed.line))

fun ChoiceTyped.plusNotChosen(line: Line): ChoiceTyped =
	ChoiceTyped(termOrNull?.eitherSecond, choice.plus(line))

val ChoiceTyped.typed: Typed
	get() =
		if (termOrNull == null) error("no choice")
		else Typed(termOrNull, type(choice))

val ChoiceTyped.decompileChosenLineTyped: LineTyped
	get() =
		choice.lineStack.linkOrNull!!.let { link ->
			termOrNull!!.either(
				{ chosenTerm ->
					LineTyped(chosenTerm, link.value)
				},
				{ notChosenTerm ->
					ChoiceTyped(notChosenTerm, Choice(link.stack)).decompileChosenLineTyped
				}
			)
		}

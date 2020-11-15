package leo21.compiled

import leo14.lambda.Term
import leo14.lambda.eitherFirst
import leo14.lambda.eitherFirst2
import leo14.lambda.eitherSecond
import leo14.lambda.eitherSecond2
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceCompiled(
	val termOrNull: Term<Prim>?,
	val choice: Choice
)

val emptyChoiceCompiled = ChoiceCompiled(null, choice())

infix fun Term<Prim>?.of(choice: Choice) = ChoiceCompiled(this, choice)

fun ChoiceCompiled.plusChosen(compiled: LineCompiled): ChoiceCompiled =
	if (termOrNull != null) error("already chosen")
	else ChoiceCompiled(compiled.term.eitherFirst, choice.plus(compiled.line))

fun ChoiceCompiled.plusNotChosen(line: Line): ChoiceCompiled =
	ChoiceCompiled(termOrNull?.eitherSecond, choice.plus(line))

val ChoiceCompiled.compiled: Compiled
	get() =
		if (termOrNull == null) error("no choice")
		else Compiled(termOrNull, type(choice))

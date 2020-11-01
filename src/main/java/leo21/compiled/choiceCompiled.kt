package leo21.compiled

import leo.base.failIfOr
import leo14.lambda.Term
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.plus
import leo21.type.type

data class ChoiceCompiled(
	val valueTermOrNull: Term<Value>?,
	val choice: Choice
)

val emptyCompiledChoice = ChoiceCompiled(null, choice())

fun ChoiceCompiled.plusChosen(compiledLine: LineCompiled): ChoiceCompiled =
	if (valueTermOrNull != null) error("already chosen")
	else ChoiceCompiled(compiledLine.valueTerm.eitherFirst, choice.plus(compiledLine.line))

fun ChoiceCompiled.plusNotChosen(line: Line): ChoiceCompiled =
	ChoiceCompiled(valueTermOrNull?.eitherSecond, choice.plus(line))

val ChoiceCompiled.compiled: Compiled
	get() =
		if (valueTermOrNull == null) error("no choice")
		else Compiled(valueTermOrNull, type(choice))

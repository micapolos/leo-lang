package leo21.compiled

import leo.base.fold
import leo14.lambda.Term
import leo14.lambda.eitherFirst2
import leo14.lambda.eitherSecond2
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Line
import leo21.type.choice
import leo21.type.isEmpty
import leo21.type.line
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
	else if (choice.isEmpty) ChoiceCompiled(compiled.term, choice.plus(compiled.line))
	else ChoiceCompiled(compiled.term.eitherFirst2, choice.plus(compiled.line))

fun ChoiceCompiled.plusNotChosen(line: Line): ChoiceCompiled =
	ChoiceCompiled(termOrNull?.eitherSecond2, choice.plus(line))

val ChoiceCompiled.compiled: Compiled
	get() =
		if (termOrNull == null) error("no choice")
		else Compiled(termOrNull, type(line(choice)))

fun ChoiceCompiled.plus(option: Option): ChoiceCompiled =
	when (option) {
		is YesOption -> plusChosen(option.lineCompiled)
		is NoOption -> plusNotChosen(option.line)
	}

fun choice(option: Option, vararg options: Option): ChoiceCompiled =
	emptyChoiceCompiled.plus(option).fold(options) { plus(it) }

package leo19.typed

import leo.base.runIf
import leo13.stack
import leo19.term.term
import leo19.type.Case
import leo19.type.Choice
import leo19.type.ChoiceType
import leo19.type.isSimple
import leo19.type.plus
import leo19.type.size

data class TypedChoice(
	val selectionOrNull: Selection?,
	val choice: Choice
)

val emptyTypedChoice = TypedChoice(null, Choice(stack()))

fun TypedChoice.plusSelected(field: TypedField) =
	if (selectionOrNull != null) error("already selected")
	else TypedChoice(field.typed.term.at(choice.size), choice.plus(field.typeCase))

fun TypedChoice.plusIgnored(case: Case) =
	TypedChoice(selectionOrNull, choice.plus(case))

val TypedChoice.typed: Typed
	get() =
		if (selectionOrNull == null) error("not selected")
		else term(selectionOrNull.index)
			.runIf(!choice.isSimple) { term(this, selectionOrNull.term) }
			.of(ChoiceType(choice))

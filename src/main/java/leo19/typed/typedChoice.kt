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
	val selectionOrNull: TypedSelection?,
	val choice: Choice
)

val emptyTypedChoice = TypedChoice(null, Choice(stack()))

fun TypedChoice.plusSelected(field: TypedField) =
	plus(TypedSelection(field, choice.size))

fun TypedChoice.plus(selection: TypedSelection) =
	if (selectionOrNull != null) error("already selected")
	else TypedChoice(selection, choice.plus(selection.field.typeCase))

fun TypedChoice.plusIgnored(case: Case) =
	TypedChoice(selectionOrNull, choice.plus(case))

val TypedChoice.typed: Typed
	get() =
		if (selectionOrNull == null) error("not selected")
		else term(selectionOrNull.caseIndex)
			.runIf(!choice.isSimple) { term(this, selectionOrNull.field.typed.term) }
			.of(ChoiceType(choice))

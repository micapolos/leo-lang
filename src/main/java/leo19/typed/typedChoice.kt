package leo19.typed

import leo.base.runIf
import leo13.stack
import leo19.term.Term
import leo19.term.nullTerm
import leo19.term.term
import leo19.type.Case
import leo19.type.Choice
import leo19.type.ChoiceType
import leo19.type.isSimple
import leo19.type.isStatic
import leo19.type.plus
import leo19.type.size

data class TypedChoice(
	val termOrNull: Term?,
	val choice: Choice
)

val emptyTypedChoice = TypedChoice(null, Choice(stack()))

fun TypedChoice.plusSelected(field: TypedField) =
	if (termOrNull != null) error("already selected")
	else TypedChoice(
		term(choice.size).runIf(!choice.isSimple) {
			term(this, field.typed.term)
		},
		choice.plus(field.typeCase))

fun TypedChoice.plusIgnored(case: Case) =
	TypedChoice(
		termOrNull?.runIf(choice.isSimple && !case.type.isStatic) {
			term(this, nullTerm)
		},
		choice.plus(case))

val TypedChoice.typed: Typed
	get() =
		if (termOrNull == null) error("not selected")
		else termOrNull.of(ChoiceType(choice))

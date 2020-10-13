package leo19.typed

import leo.base.runIf
import leo13.get
import leo13.stack
import leo19.term.ArrayTerm
import leo19.term.IntTerm
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

infix fun Term?.of(choice: Choice) = TypedChoice(this, choice)

fun TypedChoice.plusYes(field: TypedField) =
	if (termOrNull != null) error("already selected")
	else TypedChoice(
		term(choice.size).runIf(!choice.isSimple) {
			term(this, field.typed.term)
		},
		choice.plus(field.typeCase))

fun TypedChoice.plusNo(case: Case) =
	TypedChoice(
		termOrNull?.runIf(choice.isSimple && !case.type.isStatic) {
			term(this, nullTerm)
		},
		choice.plus(case))

val TypedChoice.typed: Typed
	get() =
		if (termOrNull == null) error("not selected")
		else termOrNull.of(ChoiceType(choice))

val TypedChoice.index: Int
	get() =
		if (choice.isSimple) (termOrNull as IntTerm).int
		else ((termOrNull as ArrayTerm).stack.get(1)!! as IntTerm).int

val TypedChoice.yesTerm: Term
	get() =
		if (choice.isSimple) nullTerm
		else (termOrNull as ArrayTerm).stack.get(0)!!

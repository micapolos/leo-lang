package leo19.typed

import leo13.Stack
import leo13.push
import leo13.stack
import leo19.term.Term
import leo19.type.Type

data class TypedSwitch(val termStack: Stack<Term>, val typeOrNull: Type?)

val emptyTypedSwitch = TypedSwitch(stack(), null)

fun TypedSwitch.plus(case: TypedField): TypedSwitch =
	TypedSwitch(
		termStack.push(case.typed.term),
		if (typeOrNull == null) case.typed.type
		else if (case.typed.type != typeOrNull) error("case type mismatch")
		else typeOrNull)

package leo21.typed

import leo13.fold
import leo13.reverse
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Field
import leo21.type.fieldTo
import leo21.type.name

data class FieldTyped(val valueTerm: Term<Value>, val field: Field)

infix fun String.fieldTo(typed: Typed) =
	FieldTyped(typed.valueTerm, this fieldTo typed.type)

infix fun Choice.typed(typed: LineTyped): Typed =
	choiceTyped {
		fold(lineStack.reverse) { case ->
			if (typed.line.name == case.name) plusChosen(typed)
			else plusNotChosen(case)
		}
	}

val FieldTyped.rhsTyped: Typed get() = Typed(valueTerm, field.rhs)

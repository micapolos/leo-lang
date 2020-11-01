package leo21.typed

import leo13.fold
import leo13.reverse
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Field
import leo21.type.fieldTo

data class FieldTyped(val valueTerm: Term<Value>, val field: Field)

infix fun String.fieldTo(typed: Typed) =
	FieldTyped(typed.valueTerm, this fieldTo typed.type)

infix fun Choice.typed(fieldTyped: FieldTyped): Typed =
	choiceTyped {
		fold(fieldStack.reverse) { case ->
			if (fieldTyped.field.name == case.name) plusChosen(fieldTyped)
			else plusNotChosen(case)
		}
	}

val FieldTyped.rhs get() = Typed(valueTerm, field.rhs)
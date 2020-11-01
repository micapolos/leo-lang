package leo21.compiled

import leo13.fold
import leo13.reverse
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Choice
import leo21.type.Field
import leo21.type.fieldTo

data class FieldCompiled(val valueTerm: Term<Value>, val field: Field)

infix fun String.fieldTo(compiled: Compiled) =
	FieldCompiled(compiled.valueTerm, this fieldTo compiled.type)

infix fun Choice.compiled(fieldCompiled: FieldCompiled): Compiled =
	compiledChoice {
		fold(fieldStack.reverse) { case ->
			if (fieldCompiled.field.name == case.name) plusChosen(fieldCompiled)
			else plusNotChosen(case)
		}
	}

val FieldCompiled.rhs get() = Compiled(valueTerm, field.rhs)
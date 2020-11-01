package leo21.compiled

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Field
import leo21.type.fieldTo

data class FieldCompiled(val valueTerm: Term<Value>, val field: Field)

infix fun String.fieldTo(compiled: Compiled) =
	FieldCompiled(compiled.valueTerm, this fieldTo compiled.type)
package leo21.typed

import leo13.EmptyStack
import leo13.LinkStack
import leo13.onlyOrNull
import leo14.lambda.Term
import leo14.lambda.first
import leo14.lambda.second
import leo14.lambda.value.Value
import leo21.term.nilTerm
import leo21.term.plus
import leo21.type.Struct
import leo21.type.plus
import leo21.type.struct
import leo21.type.type

data class StructTyped(
	val valueTerm: Term<Value>,
	val struct: Struct
)

val emptyStructTyped = StructTyped(nilTerm, struct())

fun StructTyped.plus(typed: FieldTyped): StructTyped =
	StructTyped(valueTerm.plus(typed.valueTerm), struct.plus(typed.field))

val StructTyped.compiled
	get() =
		Typed(valueTerm, type(struct))

fun StructTyped.field(name: String): FieldTyped =
	when (struct.fieldStack) {
		is EmptyStack -> error("no field")
		is LinkStack -> struct.fieldStack.link.let { link ->
			if (link.value.name == name) FieldTyped(valueTerm.second, link.value)
			else StructTyped(valueTerm.first, Struct(link.stack)).field(name)
		}
	}

val StructTyped.onlyField: FieldTyped
	get() =
		FieldTyped(valueTerm.second, struct.fieldStack.onlyOrNull!!)

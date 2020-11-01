package leo21.compiled

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

data class StructCompiled(
	val valueTerm: Term<Value>,
	val struct: Struct
)

val emptyCompiledStruct = StructCompiled(nilTerm, struct())

fun StructCompiled.plus(compiled: FieldCompiled): StructCompiled =
	StructCompiled(valueTerm.plus(compiled.valueTerm), struct.plus(compiled.field))

val StructCompiled.compiled
	get() =
		Compiled(valueTerm, type(struct))

fun StructCompiled.field(name: String): FieldCompiled =
	when (struct.fieldStack) {
		is EmptyStack -> error("no field")
		is LinkStack -> struct.fieldStack.link.let { link ->
			if (link.value.name == name) FieldCompiled(valueTerm.second, link.value)
			else StructCompiled(valueTerm.first, Struct(link.stack)).field(name)
		}
	}

val StructCompiled.onlyField: FieldCompiled
	get() =
		FieldCompiled(valueTerm.second, struct.fieldStack.onlyOrNull!!)

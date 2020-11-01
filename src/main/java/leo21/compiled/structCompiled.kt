package leo21.compiled

import leo14.lambda.Term
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

fun StructCompiled.plus(compiled: LineCompiled): StructCompiled =
	StructCompiled(valueTerm.plus(compiled.valueTerm), struct.plus(compiled.line))

val StructCompiled.compiled
	get() =
		Compiled(valueTerm, type(struct))
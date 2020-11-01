package leo21.compiled

import leo.base.fold
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.term.term
import leo21.type.Type
import leo21.type.arrow
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.stringType
import leo21.type.struct

data class Compiled(val valueTerm: Term<Value>, val type: Type)

fun compiled(vararg fields: FieldCompiled): Compiled =
	emptyCompiledStruct.fold(fields) { plus(it) }.compiled

fun compiledChoice(fn: ChoiceCompiled.() -> ChoiceCompiled): Compiled =
	emptyCompiledChoice.fn().compiled

fun compiled(string: String) = Compiled(term(string), stringType)
fun compiled(double: Double) = Compiled(term(double), doubleType)

val Compiled.struct get() = StructCompiled(valueTerm, type.struct)
val Compiled.choice get() = ChoiceCompiled(valueTerm, type.choice)
val Compiled.arrow get() = ArrowCompiled(valueTerm, type.arrow)

fun Compiled.get(name: String) = compiled(struct.onlyField.rhs.struct.field(name))
fun Compiled.make(name: String) = compiled(name fieldTo this)
fun Compiled.invoke(compiled: Compiled) = arrow.invoke(compiled)

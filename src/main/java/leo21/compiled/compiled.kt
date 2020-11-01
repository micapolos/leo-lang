package leo21.compiled

import leo.base.fold
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.term.term
import leo21.type.Type
import leo21.type.doubleType
import leo21.type.stringType

data class Compiled(val valueTerm: Term<Value>, val type: Type)

fun compiled(vararg fields: FieldCompiled): Compiled =
	emptyCompiledStruct.fold(fields) { plus(it) }.compiled

fun compiledChoice(fn: ChoiceCompiled.() -> ChoiceCompiled): Compiled =
	emptyCompiledChoice.fn().compiled

fun compiled(string: String) = Compiled(term(string), stringType)
fun compiled(double: Double) = Compiled(term(double), doubleType)

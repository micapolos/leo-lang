package leo21.compiled

import leo.base.fold
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Type

data class Compiled(val valueTerm: Term<Value>, val type: Type)

fun compiled(vararg lines: LineCompiled): Compiled =
	emptyCompiledStruct.fold(lines) { plus(it) }.compiled

fun compiledChoice(fn: ChoiceCompiled.() -> ChoiceCompiled): Compiled =
	emptyCompiledChoice.fn().compiled

fun compiled(string: String) = compiled(compiledLine(string))
fun compiled(double: Double) = compiled(compiledLine(double))
fun compiled(int: Int) = compiled(compiledLine(int))

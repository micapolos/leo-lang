package leo21.compiled

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.term.term
import leo21.type.Line
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.stringLine

data class LineCompiled(val valueTerm: Term<Value>, val line: Line)

fun compiledLine(string: String) = LineCompiled(term(string), stringLine)
fun compiledLine(double: Double) = LineCompiled(term(double), doubleLine)
fun compiledLine(int: Int) = compiledLine(int.toDouble())
infix fun String.lineTo(compiled: Compiled) =
	LineCompiled(compiled.valueTerm, this lineTo compiled.type)
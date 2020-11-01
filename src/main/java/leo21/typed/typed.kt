package leo21.typed

import leo.base.fold
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.term.nilTerm
import leo21.term.plus
import leo21.term.term
import leo21.type.Line
import leo21.type.Type
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.nilType
import leo21.type.plus
import leo21.type.stringLine

data class Typed(val valueTerm: Term<Value>, val type: Type)
data class TypedLine(val valueTerm: Term<Value>, val line: Line)

infix fun Term<Value>.of(type: Type) = Typed(this, type)
infix fun Term<Value>.of(line: Line) = TypedLine(this, line)

val nilTyped = Typed(nilTerm, nilType)
fun Typed.plus(typedLine: TypedLine): Typed = valueTerm.plus(typedLine.valueTerm) of type.plus(typedLine.line)
fun typed(vararg typedLines: TypedLine) = nilTyped.fold(typedLines) { plus(it) }

infix fun String.lineTo(rhs: Typed) = TypedLine(rhs.valueTerm, this lineTo rhs.type)

fun typedLine(string: String) = term(string) of stringLine
fun typedLine(double: Double) = term(double) of doubleLine
fun typedLine(int: Int) = term(int) of doubleLine

fun typed(string: String) = typed(typedLine(string))
fun typed(double: Double) = typed(typedLine(double))
fun typed(int: Int) = typed(typedLine(int))

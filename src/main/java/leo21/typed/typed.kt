package leo21.typed

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

data class Typed(val valueTerm: Term<Value>, val type: Type)

fun typed(vararg fields: FieldTyped): Typed =
	emptyStructTyped.fold(fields) { plus(it) }.compiled

fun choiceTyped(fn: ChoiceTyped.() -> ChoiceTyped): Typed =
	emptyChoiceTyped.fn().typed

fun typed(string: String) = Typed(term(string), stringType)
fun typed(double: Double) = Typed(term(double), doubleType)

val Typed.struct get() = StructTyped(valueTerm, type.struct)
val Typed.choice get() = ChoiceTyped(valueTerm, type.choice)
val Typed.arrow get() = ArrowTyped(valueTerm, type.arrow)

fun Typed.get(name: String) = typed(struct.onlyField.rhs.struct.field(name))
fun Typed.make(name: String) = typed(name fieldTo this)
fun Typed.invoke(typed: Typed) = arrow.invoke(typed)

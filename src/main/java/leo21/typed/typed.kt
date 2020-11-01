package leo21.typed

import leo.base.fold
import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.lambda.value.DoubleMinusDoubleValue
import leo14.lambda.value.DoublePlusDoubleValue
import leo14.lambda.value.DoubleTimesDoubleValue
import leo14.lambda.value.StringPlusStringValue
import leo14.lambda.value.Value
import leo21.type.ChoiceType
import leo21.type.StructType
import leo21.type.Type
import leo21.type.doubleType
import leo21.type.stringType
import leo21.type.type

data class Typed(val valueTerm: Term<Value>, val type: Type)

fun typed(typed: StructTyped) = Typed(typed.valueTerm, type(typed.struct))
fun typed(typed: ChoiceTyped) = Typed(typed.valueTermOrNull!!, type(typed.choice))

fun <R> Typed.switch(
	structFn: (StructTyped) -> R,
	choiceFn: (ChoiceTyped) -> R
): R =
	when (type) {
		is StructType -> structFn(StructTyped(valueTerm, type.struct))
		is ChoiceType -> choiceFn(ChoiceTyped(valueTerm, type.choice))
	}

fun typed(vararg lines: LineTyped): Typed =
	typed(emptyStructTyped.fold(lines) { plus(it) })

fun choiceTyped(fn: ChoiceTyped.() -> ChoiceTyped): Typed =
	typed(emptyChoiceTyped.fn())

fun typed(text: String) = typed(line(text))
fun typed(number: Double) = typed(line(number))

val Typed.structOrNull: StructTyped? get() = switch({ it }, { null })
val Typed.choiceOrNull: ChoiceTyped? get() = switch({ null }, { it })

val Typed.struct get() = structOrNull.notNullOrError("not struct")
val Typed.choice get() = choiceOrNull.notNullOrError("not choice")

fun Typed.getOrNull(name: String): Typed? =
	structOrNull?.onlyLineOrNull?.rhsOrNull?.structOrNull?.lineOrNull(name)?.let { typed(it) }

fun Typed.invokeOrNull(typed: Typed) = structOrNull?.onlyLineOrNull?.arrowTypedOrNull?.invokeOrNull(typed)

fun Typed.get(name: String) = getOrNull(name).notNullOrError("no field")
fun Typed.invoke(typed: Typed) = invokeOrNull(typed).notNullOrError("invoke")
fun Typed.make(name: String) = typed(name lineTo this)

fun Typed.plus(line: LineTyped): Typed = struct.plus(line).typed

val Typed.stringValueTerm: Term<Value>
	get() =
		structOrNull!!.onlyLineOrNull!!.stringTypedOrNull!!.valueTerm

val Typed.doubleValueTerm: Term<Value>
	get() =
		structOrNull!!.onlyLineOrNull!!.doubleTypedOrNull!!.valueTerm

fun Typed.doublePlus(typed: Typed): Typed =
	Typed(
		term(DoublePlusDoubleValue).invoke(doubleValueTerm).invoke(typed.doubleValueTerm),
		doubleType)

fun Typed.doubleMinus(typed: Typed): Typed =
	Typed(
		term(DoubleMinusDoubleValue).invoke(doubleValueTerm).invoke(typed.doubleValueTerm),
		doubleType)

fun Typed.doubleTimes(typed: Typed): Typed =
	Typed(
		term(DoubleTimesDoubleValue).invoke(doubleValueTerm).invoke(typed.doubleValueTerm),
		doubleType)

fun Typed.stringPlus(typed: Typed): Typed =
	Typed(
		term(StringPlusStringValue).invoke(stringValueTerm).invoke(typed.stringValueTerm),
		stringType)

package leo21.typed

import leo.base.fold
import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.script
import leo14.lambda.term
import leo14.lineTo
import leo14.plus
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.type.ChoiceType
import leo21.type.StructType
import leo21.type.Type
import leo21.type.doubleType
import leo21.type.script
import leo21.type.stringType
import leo21.type.type

data class Typed(val term: Term<Prim>, val type: Type) {
	override fun toString() = scriptLine.toString()
}

infix fun Term<Prim>.of(type: Type) = Typed(this, type)

val Typed.scriptLine
	get() =
		"typed" lineTo term.script { reflectScriptLine }.plus("of" lineTo type.script)

fun typed(typed: StructTyped) = Typed(typed.term, type(typed.struct))
fun typed(typed: ChoiceTyped) = Typed(typed.termOrNull!!, type(typed.choice))

fun <R> Typed.switch(
	structFn: (StructTyped) -> R,
	choiceFn: (ChoiceTyped) -> R
): R =
	when (type) {
		is StructType -> structFn(StructTyped(term, type.struct))
		is ChoiceType -> choiceFn(ChoiceTyped(term, type.choice))
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

val Typed.contentOrNull: Typed?
	get() =
		structOrNull?.onlyLineOrNull?.rhsOrNull

fun Typed.invokeOrNull(typed: Typed) = structOrNull?.onlyLineOrNull?.arrowTypedOrNull?.invokeOrNull(typed)

fun Typed.get(name: String) = getOrNull(name).notNullOrError("no field")
fun Typed.invoke(typed: Typed) = invokeOrNull(typed).notNullOrError("invoke")
fun Typed.make(name: String) = typed(name lineTo this)
val Typed.switch: SwitchTyped get() = contentOrNull?.choiceOrNull.notNullOrError("not choice").switchTyped

fun Typed.plus(line: LineTyped): Typed = struct.plus(line).typed

val Typed.stringPrimTerm: Term<Prim>
	get() =
		structOrNull!!.onlyLineOrNull!!.stringTypedOrNull!!.term

val Typed.doublePrimTerm: Term<Prim>
	get() =
		structOrNull!!.onlyLineOrNull!!.doubleTypedOrNull!!.term

fun Typed.doublePlus(typed: Typed): Typed =
	Typed(
		term(DoublePlusDoublePrim).invoke(doublePrimTerm).invoke(typed.doublePrimTerm),
		doubleType)

fun Typed.doubleMinus(typed: Typed): Typed =
	Typed(
		term(DoubleMinusDoublePrim).invoke(doublePrimTerm).invoke(typed.doublePrimTerm),
		doubleType)

fun Typed.doubleTimes(typed: Typed): Typed =
	Typed(
		term(DoubleTimesDoublePrim).invoke(doublePrimTerm).invoke(typed.doublePrimTerm),
		doubleType)

fun Typed.stringPlus(typed: Typed): Typed =
	Typed(
		term(StringPlusStringPrim).invoke(stringPrimTerm).invoke(typed.stringPrimTerm),
		stringType)

fun Typed.reference(f: Typed.() -> Typed): Typed =
	arg0<Prim>().of(type).f().let { typed ->
		fn(typed.term).invoke(term).of(typed.type)
	}

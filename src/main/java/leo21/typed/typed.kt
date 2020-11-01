package leo21.typed

import leo.base.fold
import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Type
import leo21.type.choiceOrNull
import leo21.type.structOrNull

data class Typed(val valueTerm: Term<Value>, val type: Type)

fun typed(vararg lines: LineTyped): Typed =
	emptyStructTyped.fold(lines) { plus(it) }.typed

fun choiceTyped(fn: ChoiceTyped.() -> ChoiceTyped): Typed =
	emptyChoiceTyped.fn().typed

fun typed(text: String) = typed(line(text))
fun typed(number: Double) = typed(line(number))

val Typed.structOrNull get() = type.structOrNull?.let { StructTyped(valueTerm, it) }
val Typed.choiceOrNull get() = type.choiceOrNull?.let { ChoiceTyped(valueTerm, it) }

val Typed.struct get() = structOrNull.notNullOrError("not struct")
val Typed.choice get() = choiceOrNull.notNullOrError("not choice")

fun Typed.getOrNull(name: String): Typed? =
	structOrNull?.onlyLineOrNull?.rhsOrNull?.structOrNull?.lineOrNull(name)?.let { typed(it) }

fun Typed.invokeOrNull(typed: Typed) = structOrNull?.onlyLineOrNull?.arrowTypedOrNull?.invokeOrNull(typed)

fun Typed.get(name: String) = getOrNull(name).notNullOrError("no field")
fun Typed.invoke(typed: Typed) = invokeOrNull(typed).notNullOrError("invoke")
fun Typed.make(name: String) = typed(name lineTo this)

fun Typed.plus(line: LineTyped): Typed = struct.plus(line).typed

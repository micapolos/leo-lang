package leo16

import leo.base.notNullIf
import leo15.lambda.Term
import leo15.lambda.idTerm
import leo15.lambda.valueTerm
import leo15.plus
import leo16.names.*

data class Typed(val term: Term, val type: Type)

infix fun Term.of(type: Type) = Typed(this, type)
val emptyTyped = idTerm.of(emptyValue.type)

val Typed.value get() = type.value.value(term)

fun Value.of(type: Type) =
	type.value.termOrNull(this)
		?.of(type)
		?: throw AssertionError(plus(_of(type.value)))

val Any?.nativeTyped: Typed
	get() =
		valueTerm.of(nativeType)

fun Typed.plusNativeOrNull(native: Any?): Typed? =
	notNullIf(type.isEmpty) { native.nativeTyped }

fun Typed.plus(word: String, typed: Typed): Typed =
	term.plus(typed.term) of type.value.plus(word.sentenceTo(typed.value)).type

val Typed.normalize
	get() =
		term of type.normalize

val Typed.thingOrNull: Typed?
	get() =
		type.value.force.onlySentenceOrNull?.let { sentence ->
			term of value(sentence).type
		}

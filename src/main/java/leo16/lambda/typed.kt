package leo16.lambda

import leo.base.ifOrNull
import leo15.lambda.Term
import leo15.lambda.invoke
import leo15.plus
import leo15.terms.first
import leo15.terms.second

data class Typed(val term: Term, val type: Type)
data class BodyTyped(val term: Term, val type: TypeBody)
data class LinkTyped(val term: Term, val type: TypeLink)
data class AlternativeTyped(val term: Term, val type: TypeAlternative)
data class FieldTyped(val term: Term, val type: TypeField)
data class SentenceTyped(val term: Term, val type: TypeSentence)
data class FunctionTyped(val term: Term, val type: TypeFunction)
data class NativeTyped(val term: Term, val native: Any)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun Term.of(body: TypeBody) = BodyTyped(this, body)
infix fun Term.of(link: TypeLink) = LinkTyped(this, link)
infix fun Term.of(alternative: TypeAlternative) = AlternativeTyped(this, alternative)
infix fun Term.of(field: TypeField) = FieldTyped(this, field)
infix fun Term.of(sentence: TypeSentence) = SentenceTyped(this, sentence)
infix fun Term.of(function: TypeFunction) = FunctionTyped(this, function)
infix fun Term.of(native: Any) = NativeTyped(this, native)

val Typed.body: BodyTyped
	get() =
		term of type.body

fun <R> BodyTyped.match(
	emptyFn: () -> R,
	linkFn: (LinkTyped) -> R,
	alternativeFn: (AlternativeTyped) -> R): R =
	when (type) {
		EmptyTypeBody -> emptyFn()
		is LinkTypeBody -> linkFn(term of type.link)
		is AlternativeTypeBody -> alternativeFn(term of type.alternative)
	}

val BodyTyped.linkOrNull: LinkTyped?
	get() =
		match({ null }, { it }, { null })

val LinkTyped.tail: Typed
	get() =
		(if (type.type.isStatic || type.field.isStatic) term else term.first) of type.type

val LinkTyped.head: FieldTyped
	get() =
		(if (type.type.isStatic || type.field.isStatic) term else term.second) of type.field

val LinkTyped.onlyHead
	get() =
		ifOrNull(tail.type.isEmpty) { head }

fun <R> FieldTyped.match(
	sentenceFn: (SentenceTyped) -> R,
	functionFn: (FunctionTyped) -> R,
	nativeFn: (NativeTyped) -> R) =
	when (type) {
		is SentenceTypeField -> sentenceFn(term of type.sentence)
		is FunctionTypeField -> functionFn(term of type.function)
		is NativeTypeField -> nativeFn(term of type.native)
	}

val FieldTyped.sentenceOrNull: SentenceTyped?
	get() =
		type.sentenceOrNull?.let { term of it }

val FieldTyped.functionOrNull: FunctionTyped?
	get() =
		type.functionOrNull?.let { term of it }

val FieldTyped.nativeOrNull: NativeTyped?
	get() =
		type.nativeOrNull?.let { term of it }

val SentenceTyped.rhs: Typed
	get() =
		term of type.type

val Typed.typeFunctionOrNull: FunctionTyped?
	get() =
		body.linkOrNull?.onlyHead?.functionOrNull

fun Typed.typeInvokeOrNull(typed: Typed): Typed? =
	typeFunctionOrNull?.invokeOrNull(typed)

fun FunctionTyped.invokeOrNull(typed: Typed): Typed? =
	ifOrNull(type.input == typed.type) {
		term.invoke(typed.term) of type.output
	}

fun Typed.plus(field: FieldTyped): Typed =
	(if (type.isStatic)
		if (field.type.isStatic) term
		else field.term
	else
		if (field.type.isStatic) term
		else term.plus(field.term)) of type.plus(field.type)

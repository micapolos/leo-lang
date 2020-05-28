package leo16.lambda.typed

import leo.base.fold
import leo.base.ifOrNull
import leo15.lambda.Term
import leo15.lambda.idTerm
import leo15.lambda.invoke
import leo15.plus
import leo15.terms.first
import leo15.terms.second
import leo15.terms.term
import leo16.lambda.type.AlternativeTypeBody
import leo16.lambda.type.EmptyTypeBody
import leo16.lambda.type.FunctionTypeField
import leo16.lambda.type.LinkTypeBody
import leo16.lambda.type.NativeTypeField
import leo16.lambda.type.SentenceTypeField
import leo16.lambda.type.Type
import leo16.lambda.type.TypeAlternative
import leo16.lambda.type.TypeBody
import leo16.lambda.type.TypeField
import leo16.lambda.type.TypeFunction
import leo16.lambda.type.TypeLink
import leo16.lambda.type.TypeSentence
import leo16.lambda.type.emptyType
import leo16.lambda.type.field
import leo16.lambda.type.functionOrNull
import leo16.lambda.type.intTypeField
import leo16.lambda.type.invoke
import leo16.lambda.type.isEmpty
import leo16.lambda.type.isStatic
import leo16.lambda.type.nativeOrNull
import leo16.lambda.type.nativeTypeField
import leo16.lambda.type.plus
import leo16.lambda.type.sentenceOrNull
import leo16.lambda.type.sentenceTo
import leo16.lambda.type.stringTypeField
import leo16.lambda.type.type
import leo16.names.*

data class Typed(val term: Term, val type: Type)
data class BodyTyped(val term: Term, val body: TypeBody)
data class LinkTyped(val term: Term, val link: TypeLink)
data class AlternativeTyped(val term: Term, val alternative: TypeAlternative)
data class FieldTyped(val term: Term, val field: TypeField)
data class SentenceTyped(val term: Term, val sentence: TypeSentence)
data class FunctionTyped(val term: Term, val function: TypeFunction)
data class NativeTyped(val term: Term, val native: Any)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun Term.of(body: TypeBody) = BodyTyped(this, body)
infix fun Term.of(link: TypeLink) = LinkTyped(this, link)
infix fun Term.of(alternative: TypeAlternative) = AlternativeTyped(this, alternative)
infix fun Term.of(field: TypeField) = FieldTyped(this, field)
infix fun Term.of(sentence: TypeSentence) = SentenceTyped(this, sentence)
infix fun Term.of(function: TypeFunction) = FunctionTyped(this, function)
infix fun Term.of(native: Any) = NativeTyped(this, native)

val emptyTyped
	get() =
		idTerm of emptyType

val Typed.bodyTyped: BodyTyped
	get() =
		term of type.body

fun <R> BodyTyped.match(
	emptyFn: () -> R,
	linkFn: (LinkTyped) -> R,
	alternativeFn: (AlternativeTyped) -> R): R =
	when (body) {
		EmptyTypeBody -> emptyFn()
		is LinkTypeBody -> linkFn(term of body.link)
		is AlternativeTypeBody -> alternativeFn(term of body.alternative)
	}

val BodyTyped.linkTypedOrNull: LinkTyped?
	get() =
		match({ null }, { it }, { null })

val Typed.alternativeTypedOrNull: AlternativeTyped?
	get() =
		bodyTyped.match({ null }, { null }, { it })

val LinkTyped.previousTyped: Typed
	get() =
		(if (link.previousType.isStatic || link.lastField.isStatic) term else term.first) of link.previousType

val LinkTyped.lastFieldTyped: FieldTyped
	get() =
		(if (link.previousType.isStatic || link.lastField.isStatic) term else term.second) of link.lastField

val LinkTyped.onlyFieldTyped
	get() =
		ifOrNull(previousTyped.type.isEmpty) { lastFieldTyped }

fun <R> FieldTyped.match(
	sentenceFn: (SentenceTyped) -> R,
	functionFn: (FunctionTyped) -> R,
	nativeFn: (NativeTyped) -> R) =
	when (field) {
		is SentenceTypeField -> sentenceFn(term of field.sentence)
		is FunctionTypeField -> functionFn(term of field.function)
		is NativeTypeField -> nativeFn(term of field.native)
	}

val FieldTyped.sentenceOrNull: SentenceTyped?
	get() =
		field.sentenceOrNull?.let { term of it }

val FieldTyped.functionOrNull: FunctionTyped?
	get() =
		field.functionOrNull?.let { term of it }

val FieldTyped.nativeOrNull: NativeTyped?
	get() =
		field.nativeOrNull?.let { term of it }

val SentenceTyped.rhsTyped: Typed
	get() =
		term of sentence.type

val Typed.typeFunctionOrNull: FunctionTyped?
	get() =
		bodyTyped.linkTypedOrNull?.onlyFieldTyped?.functionOrNull

fun Typed.typeInvokeOrNull(typed: Typed): Typed? =
	typeFunctionOrNull?.invokeOrNull(typed)

fun FunctionTyped.invokeOrNull(typed: Typed): Typed? =
	ifOrNull(function.input == typed.type) {
		term.invoke(typed.term) of function.output
	}

fun Typed.plus(field: FieldTyped): Typed =
	(if (type.isStatic)
		if (field.field.isStatic) term
		else field.term
	else
		if (field.field.isStatic) term
		else term.plus(field.term)) of type.plus(field.field)

fun Typed.plusOrNull(typed: Typed): Typed? =
	typed.bodyTyped.match(
		{ this },
		{ plusOrNull(it.previousTyped)?.plus(it.lastFieldTyped) },
		{ null })

fun String.sentenceTo(typed: Typed): SentenceTyped =
	typed.term of sentenceTo(typed.type)

fun String.fieldTo(typed: Typed): FieldTyped =
	sentenceTo(typed).fieldTyped

val SentenceTyped.fieldTyped: FieldTyped
	get() =
		term of sentence.field

operator fun String.invoke(vararg fields: FieldTyped): FieldTyped =
	fieldTo(typed(*fields))

operator fun String.invoke(typed: Typed): FieldTyped =
	fieldTo(typed)

fun typed(vararg fields: FieldTyped): Typed =
	emptyTyped.fold(fields) { plus(it) }

val NativeTyped.fieldTyped: FieldTyped
	get() =
		term of native.nativeTypeField

val FieldTyped.typed
	get() =
		term of type(field)

val Int.typedField get() = term of intTypeField
val String.typedField get() = term of stringTypeField
val Boolean.typedField
	get() = _boolean(
		if (this) type(_false(type())).or(typed(_true(typed())))
		else typed(_false(typed())).or(type(_true(type()))))

val Typed.isEmpty get() = type.isEmpty
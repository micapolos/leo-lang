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
import leo16.lambda.type.FunctionTypeBody
import leo16.lambda.type.LazyTypeBody
import leo16.lambda.type.LinkTypeBody
import leo16.lambda.type.NativeTypeBody
import leo16.lambda.type.RepeatTypeBody
import leo16.lambda.type.RepeatingTypeBody
import leo16.lambda.type.Type
import leo16.lambda.type.TypeAlternative
import leo16.lambda.type.TypeBody
import leo16.lambda.type.TypeFunction
import leo16.lambda.type.TypeLazy
import leo16.lambda.type.TypeLink
import leo16.lambda.type.TypeRepeating
import leo16.lambda.type.TypeSentence
import leo16.lambda.type.emptyType
import leo16.lambda.type.intType
import leo16.lambda.type.invoke
import leo16.lambda.type.isEmpty
import leo16.lambda.type.isStatic
import leo16.lambda.type.nativeTypeBody
import leo16.lambda.type.plus
import leo16.lambda.type.sentenceTo
import leo16.lambda.type.stringType
import leo16.lambda.type.type
import leo16.names.*

data class Typed(val term: Term, val type: Type)
data class BodyTyped(val term: Term, val body: TypeBody)
data class LinkTyped(val term: Term, val link: TypeLink)
data class AlternativeTyped(val term: Term, val alternative: TypeAlternative)
data class SentenceTyped(val term: Term, val sentence: TypeSentence)
data class FunctionTyped(val term: Term, val function: TypeFunction)
data class NativeTyped(val term: Term, val native: Any)
data class LazyTyped(val term: Term, val lazy: TypeLazy)
data class RepeatingTyped(val term: Term, val repeating: TypeRepeating)

infix fun Term.of(type: Type) = Typed(this, type)
infix fun Term.of(body: TypeBody) = BodyTyped(this, body)
infix fun Term.of(link: TypeLink) = LinkTyped(this, link)
infix fun Term.of(alternative: TypeAlternative) = AlternativeTyped(this, alternative)
infix fun Term.of(sentence: TypeSentence) = SentenceTyped(this, sentence)
infix fun Term.of(function: TypeFunction) = FunctionTyped(this, function)
infix fun Term.of(native: Any) = NativeTyped(this, native)
infix fun Term.of(lazy: TypeLazy) = LazyTyped(this, lazy)
infix fun Term.of(repeating: TypeRepeating) = RepeatingTyped(this, repeating)

val emptyTyped
	get() =
		idTerm of emptyType

val Typed.bodyTyped: BodyTyped
	get() =
		term of type.body

fun <R> BodyTyped.match(
	emptyFn: () -> R,
	linkFn: (LinkTyped) -> R,
	alternativeFn: (AlternativeTyped) -> R,
	functionFn: (FunctionTyped) -> R,
	nativeFn: (NativeTyped) -> R,
	lazyFn: (LazyTyped) -> R,
	repeatingFn: (RepeatingTyped) -> R,
	repeatFn: () -> R): R =
	when (body) {
		EmptyTypeBody -> emptyFn()
		is LinkTypeBody -> linkFn(term of body.link)
		is AlternativeTypeBody -> alternativeFn(term of body.alternative)
		is FunctionTypeBody -> functionFn(term of body.function)
		is NativeTypeBody -> nativeFn(term of body.native)
		is LazyTypeBody -> lazyFn(term of body.lazy)
		is RepeatingTypeBody -> repeatingFn(term of body.repeating)
		RepeatTypeBody -> repeatFn()
	}

val BodyTyped.linkTypedOrNull: LinkTyped?
	get() =
		match({ null }, { it }, { null }, { null }, { null }, { null }, { null }, { null })

val Typed.alternativeTypedOrNull: AlternativeTyped?
	get() =
		bodyTyped.match({ null }, { null }, { it }, { null }, { null }, { null }, { null }, { null })

val Typed.functionTypedOrNull: FunctionTyped?
	get() =
		bodyTyped.match({ null }, { null }, { null }, { it }, { null }, { null }, { null }, { null })

val Typed.nativeTypedOrNull: NativeTyped?
	get() =
		bodyTyped.match({ null }, { null }, { null }, { null }, { it }, { null }, { null }, { null })

val Typed.repeatingTypedOrNull: RepeatingTyped?
	get() =
		bodyTyped.match({ null }, { null }, { null }, { null }, { null }, { null }, { it }, { null })

val LinkTyped.previousTyped: Typed
	get() =
		(if (link.previousType.isStatic || link.lastSentence.isStatic) term else term.first) of link.previousType

val LinkTyped.lastSentenceTyped: SentenceTyped
	get() =
		(if (link.previousType.isStatic || link.lastSentence.isStatic) term else term.second) of link.lastSentence

val LinkTyped.onlySentenceTyped
	get() =
		ifOrNull(previousTyped.type.isEmpty) { lastSentenceTyped }

val SentenceTyped.rhsTyped: Typed
	get() =
		term of sentence.type

fun Typed.typeInvokeOrNull(typed: Typed): Typed? =
	functionTypedOrNull?.invokeOrNull(typed)

fun FunctionTyped.invokeOrNull(typed: Typed): Typed? =
	ifOrNull(function.parameterType == typed.type) {
		term.invoke(typed.term) of function.resultType
	}

fun Typed.plus(sentenceTyped: SentenceTyped): Typed =
	(if (type.isStatic)
		if (sentenceTyped.sentence.isStatic) term
		else sentenceTyped.term
	else
		if (sentenceTyped.sentence.isStatic) term
		else term.plus(sentenceTyped.term)) of type.plus(sentenceTyped.sentence)

fun Typed.plusOrNull(typed: Typed): Typed? =
	typed.bodyTyped.linkTypedOrNull?.run {
		plusOrNull(previousTyped)?.plus(lastSentenceTyped)
	}

fun String.sentenceTo(typed: Typed): SentenceTyped =
	typed.term of sentenceTo(typed.type)

operator fun String.invoke(vararg fields: SentenceTyped): SentenceTyped =
	sentenceTo(typed(*fields))

operator fun String.invoke(typed: Typed): SentenceTyped =
	sentenceTo(typed)

fun typed(vararg sentences: SentenceTyped): Typed =
	emptyTyped.fold(sentences) { plus(it) }

val NativeTyped.bodyTyped: BodyTyped
	get() =
		term of native.nativeTypeBody

val Int.typedField get() = term of intType
val String.typedField get() = term of stringType
val Boolean.typedField
	get() = _boolean(
		if (this) type(_false(type())).or(typed(_true(typed())))
		else typed(_false(typed())).or(type(_true(type()))))

val Typed.isEmpty get() = type.isEmpty
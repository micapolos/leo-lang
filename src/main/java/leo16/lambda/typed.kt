package leo16.lambda

import leo.base.ifOrNull
import leo.base.notNullIf
import leo15.lambda.Term
import leo15.lambda.invoke
import leo15.plus
import leo15.terms.first
import leo15.terms.second

data class Typed<out T>(val term: Term, val type: T)

infix fun <T> Term.of(type: T) = Typed(this, type)

val Typed<Type>.typeBody: Typed<TypeBody>
	get() =
		term of type.body

val Typed<TypeBody>.bodyLinkOrNull: Typed<TypeLink>?
	get() =
		type.linkOrNull?.let { term of it }

val Typed<TypeBody>.bodyAlternativeOrNull: Typed<TypeAlternative>?
	get() =
		type.alternativeOrNull?.let { term of it }

val Typed<TypeBody>.bodyOnlyFieldOrNull: Typed<TypeField>?
	get() =
		bodyLinkOrNull?.let { link ->
			notNullIf(link.type.type.isEmpty) {
				link.linkField
			}
		}

fun <R> Typed<TypeBody>.bodyMatch(
	emptyFn: () -> R,
	linkFn: (Typed<TypeLink>) -> R,
	alternativeFn: (Typed<TypeAlternative>) -> R): R =
	when (type) {
		EmptyTypeBody -> emptyFn()
		is LinkTypeBody -> linkFn(term of type.link)
		is AlternativeTypeBody -> alternativeFn(term of type.alternative)
	}

val Typed<TypeLink>.linkType: Typed<Type>
	get() =
		(if (type.type.isStatic || type.field.isStatic) term else term.first) of type.type

val Typed<TypeLink>.linkField: Typed<TypeField>
	get() =
		(if (type.type.isStatic || type.field.isStatic) term else term.second) of type.field

val Typed<TypeField>.fieldSentenceOrNull: Typed<TypeSentence>?
	get() =
		type.sentenceOrNull?.let { term of it }

val Typed<TypeField>.fieldFunctionOrNull: Typed<TypeFunction>?
	get() =
		type.functionOrNull?.let { term of it }

val Typed<TypeField>.fieldNativeOrNull: Typed<Any>?
	get() =
		type.nativeOrNull?.let { term of it }

val Typed<TypeSentence>.sentenceType: Typed<Type>
	get() =
		term of type.type

val Typed<Type>.typeFunctionOrNull: Typed<TypeFunction>?
	get() =
		typeBody.bodyOnlyFieldOrNull?.fieldFunctionOrNull

fun Typed<Type>.typeInvokeOrNull(typed: Typed<Type>): Typed<Type>? =
	typeFunctionOrNull?.functionInvokeOrNull(typed)

fun Typed<TypeFunction>.functionInvokeOrNull(typed: Typed<Type>): Typed<Type>? =
	ifOrNull(type.input == typed.type) {
		term.invoke(typed.term) of type.output
	}

fun Typed<Type>.typePlus(field: Typed<TypeField>): Typed<Type> =
	(if (type.isStatic)
		if (field.type.isStatic) term
		else field.term
	else
		if (field.type.isStatic) term
		else term.plus(field.term)) of type.plus(field.type)

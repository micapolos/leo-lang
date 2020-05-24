package leo16.lambda

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.StackLink
import leo13.asStack
import leo13.fold
import leo13.reverse
import leo15.lambda.Term
import leo15.lambda.invoke
import leo15.plus
import leo15.terms.first
import leo15.terms.second

data class Typed<out T>(val term: Term, val type: T)

infix fun <T> Term.of(type: T) = Typed(this, type)

val Typed<Type>.typeChoice: Typed<TypeChoice>
	get() =
		term of type.choice

val Typed<TypeChoice>.choiceOnlyCaseOrNull: Typed<TypeCase>?
	get() =
		type.onlyCaseOrNull?.let { typeCase ->
			term of typeCase
		}

fun Typed<TypeChoice>.plusMatch(whenTermLink: StackLink<Term>, type: Type): Typed<Type>? =
	term.fold(whenTermLink.asStack.reverse) { invoke(it) } of type

val Typed<TypeCase>.caseLinkOrNull: Typed<TypeLink>?
	get() =
		when (type) {
			EmptyTypeCase -> null
			is LinkTypeCase -> term of type.link
		}

val Typed<TypeCase>.caseOnlyFieldOrNull: Typed<TypeField>?
	get() =
		caseLinkOrNull?.let { link ->
			notNullIf(link.type.type.isEmpty) {
				link.linkField
			}
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

val Typed<TypeSentence>.sentenceType: Typed<Type>
	get() =
		term of type.type

val Typed<Type>.typeFunctionOrNull: Typed<TypeFunction>?
	get() =
		typeChoice.choiceOnlyCaseOrNull?.caseOnlyFieldOrNull?.fieldFunctionOrNull

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

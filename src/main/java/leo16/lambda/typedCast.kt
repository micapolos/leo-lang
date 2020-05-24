package leo16.lambda

import leo.base.ifOrNull
import leo.base.notNullIf
import leo15.lambda.Term
import leo15.lambda.choiceTerm
import leo15.plus

data class Cast(val termOrNull: Term?)

fun Cast.term(inputTerm: Term) = termOrNull ?: inputTerm

fun Typed<Type>.typeCast(type: Type): Cast? =
	typeCast(type.body)

fun Typed<Type>.typeCast(body: TypeBody): Cast? =
	when (body) {
		EmptyTypeBody -> notNullIf(type.isEmpty) { Cast(null) }
		is LinkTypeBody -> typeBody.bodyLinkOrNull?.linkCast(body.link)
		is AlternativeTypeBody -> TODO()
	}

fun Typed<TypeLink>.linkCast(link: TypeLink): Cast? =
	linkType.typeCast(link.type)?.let { typeCast ->
		linkField.fieldCast(link.field)?.let { fieldCast ->
			Cast(
				if (typeCast.termOrNull == null)
					if (fieldCast.termOrNull == null) null
					else fieldCast.termOrNull
				else
					if (fieldCast.termOrNull == null) typeCast.termOrNull
					else typeCast.termOrNull.plus(fieldCast.termOrNull))
		}
	}

fun Typed<TypeField>.fieldCast(field: TypeField): Cast? =
	when (field) {
		is SentenceTypeField -> fieldSentenceOrNull?.sentenceCast(field.sentence)
		is FunctionTypeField -> fieldFunctionOrNull?.functionCast(field.function)
		is NativeTypeField -> fieldNativeOrNull?.nativeCast(field.native)
	}

fun Typed<TypeSentence>.sentenceCast(sentence: TypeSentence): Cast? =
	ifOrNull(type.word == sentence.word) {
		sentenceType.typeCast(sentence.type)
	}

fun Typed<TypeFunction>.functionCast(function: TypeFunction): Cast? =
	// TODO: Perform widening and narrowing
	ifOrNull(type == function) {
		Cast(null)
	}

fun Typed<Type>.typeCast(alternative: TypeAlternative): Cast? =
	null
		?: typeCast(alternative.firstType)?.let { cast -> Cast(choiceTerm(2, 0, cast.term(term))) }
		?: typeCast(alternative.secondType)?.let { cast -> Cast(choiceTerm(2, 1, cast.term(term))) }
		?: TODO()

fun Typed<Any>.nativeCast(native: Any): Cast? =
	ifOrNull(type == native) {
		Cast(null)
	}


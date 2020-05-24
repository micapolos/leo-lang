package leo16.lambda

import leo15.lambda.unsafeUnchoice
import leo15.lambda.value
import leo16.Field
import leo16.Value
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.plus

val Typed<Type>.typeValue: Value
	get() =
		typeBody.bodyValue

val Typed<TypeBody>.bodyValue: Value
	get() =
		bodyMatch(
			{ emptyValue },
			{ it.linkValue },
			{ it.alternativeValue }
		)

val Typed<TypeLink>.linkValue: Value
	get() =
		linkType.typeValue.plus(linkField.fieldValueField)

val Typed<TypeAlternative>.alternativeValue: Value
	get() =
		term.unsafeUnchoice(2).run {
			if (index == 0) (value of type.firstType).typeValue
			else (value of type.secondType).typeValue
		}

val Typed<TypeField>.fieldValueField: Field
	get() =
		when (type) {
			is SentenceTypeField -> (term of type.sentence).sentenceValueField
			is FunctionTypeField -> (term of type.function).functionValueField
			is NativeTypeField -> (term of type.native).nativeValueField
		}

val Typed<TypeSentence>.sentenceValueField: Field
	get() =
		type.word.invoke(sentenceType.typeValue)

val Typed<TypeFunction>.functionValueField: Field
	get() =
		_taking(type.input.asValue.plus(_giving(type.output.asValue)))

val Typed<Any>.nativeValueField: Field
	get() =
		term.value!!.nativeField

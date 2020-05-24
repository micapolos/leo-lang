package leo16.compiler

import leo.base.runIfNotNull
import leo13.EmptyStack
import leo13.LinkStack
import leo13.linkOrNull
import leo13.mapOrNull
import leo16.Field
import leo16.Sentence
import leo16.Value
import leo16.matchPrefix
import leo16.names.*
import leo16.sentenceOrNull
import leo16.value

val Value.typeOrNull: Type?
	get() =
		null
			?: typeChoiceOrNull?.type
			?: typeCaseOrNull?.type

val Value.typeChoiceOrNull: TypeChoice?
	get() =
		matchPrefix(_choice) { rhs -> rhs.casesTypeChoiceOrNull }

val Value.typeCaseOrNull: TypeCase?
	get() =
		when (fieldStack) {
			is EmptyStack -> emptyTypeCase
			is LinkStack ->
				fieldStack.link.stack.value.typeOrNull
					?.runIfNotNull(fieldStack.link.value.typeFieldOrNull) {
						linkTo(it).case
					}
		}

val Field.typeFieldOrNull: TypeField?
	get() =
		sentenceOrNull?.typeSentenceOrNull?.field

val Sentence.typeSentenceOrNull: TypeSentence?
	get() =
		word.runIfNotNull(value.typeOrNull) { sentenceTo(it) }

val Value.casesTypeChoiceOrNull: TypeChoice?
	get() =
		fieldStack.mapOrNull { typeCaseOrNull }?.linkOrNull?.choice

val Field.typeCaseOrNull: TypeCase?
	get() =
		matchPrefix(_case) { rhs ->
			rhs.typeCaseOrNull
		}

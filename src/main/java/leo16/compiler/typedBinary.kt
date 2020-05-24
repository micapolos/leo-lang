package leo16.compiler

import leo.base.bitCount
import leo.base.ifOrNull
import leo.base.indexed
import leo.base.mapFirstOrNull
import leo.base.orNullIf
import leo.base.runIfNotNull
import leo13.asStack
import leo13.linkOrNull
import leo13.reverse
import leo13.seq
import leo13.size
import leo16.Field
import leo16.Sentence
import leo16.Value
import leo16.isEmpty
import leo16.sentenceOrNull
import leo16.value

fun Value.binary(type: Type) = emptyBinary.plus(this, type)

fun Binary.plus(value: Value, type: Type): Binary? =
	align(type.alignment).plus(value, type.choice)

fun Binary.plus(value: Value, typeChoice: TypeChoice): Binary? =
	typeChoice.caseStackLink.asStack.size.bitCount.let { caseBitCount ->
		typeChoice.caseStackLink.asStack.reverse.seq.indexed.mapFirstOrNull {
			this@plus
				.plusWithBitCount(index, caseBitCount)
				.plus(value, this.value)
		}
	}

fun Binary.plus(value: Value, typeCase: TypeCase): Binary? =
	when (typeCase) {
		EmptyTypeCase -> orNullIf(!value.isEmpty)
		is LinkTypeCase -> value.fieldStack.linkOrNull
			?.let { valueFieldStack ->
				this
					.plus(valueFieldStack.stack.value, typeCase.link.type)
					?.plus(valueFieldStack.value, typeCase.link.field)
			}
	}

fun Binary.plus(field: Field, typeField: TypeField): Binary? =
	when (typeField) {
		is SentenceTypeField -> runIfNotNull(field.sentenceOrNull) { plus(it, typeField.sentence) }
		is FunctionTypeField -> TODO()
	}

fun Binary.plus(sentence: Sentence, typeSentence: TypeSentence): Binary? =
	ifOrNull(sentence.word == typeSentence.word) {
		plus(sentence.value, typeSentence.type)
	}

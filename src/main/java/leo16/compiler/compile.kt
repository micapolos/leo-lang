package leo16.compiler

import leo.base.ifOrNull
import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.asStack
import leo13.linkOrNull
import leo13.reverse
import leo13.seq
import leo16.Field
import leo16.Sentence
import leo16.Value
import leo16.sentenceOrNull
import leo16.value

fun Value.compile(type: Type): Memory? =
	type.size.sizeMemory.startPointer.write(this, type)?.memory

fun Pointer.write(value: Value, type: Type): Pointer? =
	this.write(value, type.choice)

fun Pointer.write(value: Value, typeChoice: TypeChoice): Pointer? =
	if (typeChoice.hasOneCase) write(value, typeChoice.caseStackLink.value)
	else typeChoice.caseStackLink.asStack.reverse.seq.indexed.mapFirstOrNull {
		this@write
			.write(index)
			.write(value, this.value)
			?.writeZeros(typeChoice.casesSize.minus(this.value.size))
	}

fun Pointer.write(value: Value, typeCase: TypeCase): Pointer? =
	when (typeCase) {
		EmptyTypeCase -> this
		is LinkTypeCase -> value.fieldStack.linkOrNull
			?.let { valueFieldStack ->
				this
					.write(valueFieldStack.stack.value, typeCase.link.type)
					?.write(valueFieldStack.value, typeCase.link.field)
			}
	}

fun Pointer.write(field: Field, typeField: TypeField): Pointer? =
	when (typeField) {
		is SentenceTypeField -> field.sentenceOrNull?.let { write(it, typeField.sentence) }
		is FunctionTypeField -> TODO()
	}

fun Pointer.write(sentence: Sentence, typeSentence: TypeSentence): Pointer? =
	ifOrNull(sentence.word == typeSentence.word) {
		write(sentence.value, typeSentence.type)
	}

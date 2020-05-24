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

fun Value.compile(type: Type): Compiled? =
	emptyCompiled.plus(this, type)

fun Compiled.plus(value: Value, type: Type): Compiled? =
	plus(value, type.choice)

fun Compiled.plus(value: Value, typeChoice: TypeChoice): Compiled? =
	if (typeChoice.hasOneCase) plus(value, typeChoice.caseStackLink.value)
	else typeChoice.caseStackLink.asStack.reverse.seq.indexed.mapFirstOrNull {
		this@plus
			.plus(index.primitive)
			.plus(value, this.value)
			?.plusZeros(typeChoice.casesSize.minus(this.value.size))
	}

fun Compiled.plus(value: Value, typeCase: TypeCase): Compiled? =
	when (typeCase) {
		EmptyTypeCase -> this
		is LinkTypeCase -> value.fieldStack.linkOrNull
			?.let { valueFieldStack ->
				this
					.plus(valueFieldStack.stack.value, typeCase.link.type)
					?.plus(valueFieldStack.value, typeCase.link.field)
			}
	}

fun Compiled.plus(field: Field, typeField: TypeField): Compiled? =
	when (typeField) {
		is SentenceTypeField -> field.sentenceOrNull?.let { plus(it, typeField.sentence) }
		is FunctionTypeField -> TODO()
	}

fun Compiled.plus(sentence: Sentence, typeSentence: TypeSentence): Compiled? =
	ifOrNull(sentence.word == typeSentence.word) {
		plus(sentence.value, typeSentence.type)
	}

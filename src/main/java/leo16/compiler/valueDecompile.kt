package leo16.compiler

import leo.base.Effect
import leo.base.bind
import leo.base.effect
import leo.base.update
import leo.base.updateState
import leo13.asStack
import leo13.get
import leo13.reverse
import leo16.Value
import leo16.emptyValue
import leo16.field
import leo16.plus
import leo16.sentenceTo

fun Memory.decompile(type: Type): Value? =
	startPointer.decompile(type)

fun Pointer.decompile(type: Type): Value? =
	emptyValue.plus(this, type)?.value

fun Value.plus(pointer: Pointer, type: Type): Effect<Pointer, Value>? =
	plus(pointer, type.choice)

fun Value.plus(pointer: Pointer, choice: TypeChoice): Effect<Pointer, Value>? =
	if (choice.hasOneCase) plus(pointer, choice.caseStackLink.value)
	else pointer.readInt.bind { index ->
		choice.caseStackLink.asStack.reverse.get(index)?.let { case ->
			plus(this, choice, case)
		}
	}

fun Value.plus(pointer: Pointer, choice: TypeChoice, case: TypeCase): Effect<Pointer, Value>? =
	plus(pointer, case)?.updateState {
		skip(choice.slackSize(case))
	}

fun Value.plus(pointer: Pointer, case: TypeCase): Effect<Pointer, Value>? =
	when (case) {
		EmptyTypeCase -> pointer effect this
		is LinkTypeCase -> plus(pointer, case.link.type)?.bind { value ->
			value.plus(this, case.link.field)
		}
	}

fun Value.plus(pointer: Pointer, typeField: TypeField): Effect<Pointer, Value>? =
	when (typeField) {
		is SentenceTypeField -> plus(pointer, typeField.sentence)
		is FunctionTypeField -> TODO()
	}

fun Value.plus(pointer: Pointer, typeSentence: TypeSentence): Effect<Pointer, Value>? =
	emptyValue.plus(pointer, typeSentence.type)?.update { value ->
		plus(typeSentence.word.sentenceTo(value).field)
	}

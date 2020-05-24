package leo16.compiler

import leo13.asStack
import leo13.fold

val TypeChoice.alignment: Alignment
	get() =
		indexAlignment.fold(caseStackLink.asStack) { and(it.alignment) }

val TypeCase.alignment: Alignment
	get() =
		when (this) {
			EmptyTypeCase -> Alignment.BYTE
			is LinkTypeCase -> link.type.alignment and link.field.alignment
		}

val TypeField.alignment: Alignment
	get() =
		when (this) {
			is SentenceTypeField -> sentence.alignment
			is FunctionTypeField -> function.alignment
		}

val TypeSentence.alignment: Alignment
	get() =
		type.alignment

val TypeFunction.alignment: Alignment
	get() =
		indexAlignment

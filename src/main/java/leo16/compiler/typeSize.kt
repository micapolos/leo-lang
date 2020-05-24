package leo16.compiler

import leo.base.runIf
import leo13.asStack
import leo13.fold
import kotlin.math.max

val indexSize = 4

val TypeChoice.size: Int
	get() =
		casesSize.runIf(!hasOneCase) { plus(indexSize) }

val TypeChoice.casesSize: Int
	get() =
		0.fold(caseStackLink.asStack) { max(this, it.size) }

val TypeCase.size: Int
	get() =
		when (this) {
			EmptyTypeCase -> 0
			is LinkTypeCase -> link.type.size + link.field.size
		}

val TypeField.size: Int
	get() =
		when (this) {
			is SentenceTypeField -> sentence.size
			is FunctionTypeField -> function.size
		}

val TypeSentence.size: Int
	get() =
		type.size

val TypeFunction.size: Int
	get() =
		indexAlignment.size

package leo16.compiler

import leo.base.bitCount
import leo13.asStack
import leo13.fold
import leo13.size
import kotlin.math.max

val TypeChoice.bitCount: Int
	get() =
		// TODO: Consider alignment!!!
		indexBitCount + casesBitCount

val TypeChoice.casesBitCount: Int
	get() =
		0.fold(caseStackLink.asStack) { max(this, it.bitCount) }

val TypeChoice.indexBitCount: Int
	get() =
		caseStackLink.asStack.size.bitCount

val TypeCase.bitCount: Int
	get() =
		when (this) {
			EmptyTypeCase -> 0
			is LinkTypeCase -> link.type.size + link.field.bitCount
		}

val TypeField.bitCount: Int
	get() =
		when (this) {
			is SentenceTypeField -> sentence.bitCount
			is FunctionTypeField -> function.bitCount
		}

val TypeSentence.bitCount: Int
	get() =
		type.size

val TypeFunction.bitCount: Int
	get() =
		pointerAlignment.bitCount

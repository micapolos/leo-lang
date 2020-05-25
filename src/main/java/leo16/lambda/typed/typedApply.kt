package leo16.lambda.typed

import leo.base.ifOrNull
import leo16.names.*

fun Typed.apply(sentenceTyped: SentenceTyped): Typed? =
	when (sentenceTyped.sentence.word) {
		_content -> ifOrNull(isEmpty) { sentenceTyped.rhsTyped.contentOrNull }
		_quote -> plusOrNull(sentenceTyped.rhsTyped)
		_meta -> plusOrNull(sentenceTyped.rhsTyped)
		_this -> plusOrNull(sentenceTyped.rhsTyped)
		_nothing -> ifOrNull(isEmpty && sentenceTyped.rhsTyped.isEmpty) { emptyTyped }
		_equals -> TODO()
		_hash -> TODO()
		else -> null
	}
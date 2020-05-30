package leo16.lambda.typed

import leo.base.ifOrNull
import leo16.names.*

val Typed.applyOrNull: Typed?
	get() =
		bodyTyped.linkTypedOrNull?.let { linkTyped ->
			linkTyped.lastFieldTyped.sentenceOrNull?.let { sentenceTyped ->
				when (sentenceTyped.sentence.word) {
					_thing -> ifOrNull(isEmpty) { sentenceTyped.rhsTyped.thingOrNull }
					_quote -> plusOrNull(sentenceTyped.rhsTyped)
					_meta -> plusOrNull(sentenceTyped.rhsTyped)
					_this -> plusOrNull(sentenceTyped.rhsTyped)
					_nothing -> ifOrNull(isEmpty && sentenceTyped.rhsTyped.isEmpty) { emptyTyped }
					_equals -> TODO()
					_hash -> TODO()
					_assert -> TODO()
					else -> null
				}
			}
		}

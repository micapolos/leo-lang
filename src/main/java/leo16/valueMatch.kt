package leo16

import leo.base.ifOrNull
import leo13.linkOrNull
import leo13.onlyOrNull
import leo14.Literal
import leo14.Number
import leo14.numberOrNull
import leo14.stringOrNull

fun <R : Any> Value.matchEmpty(fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

fun <R : Any> Value.matchText(fn: (String) -> R?): R? =
	fieldStack.onlyOrNull?.matchText(fn)

fun <R : Any> Value.matchNumber(fn: (Number) -> R?): R? =
	fieldStack.onlyOrNull?.matchNumber(fn)

fun <R : Any> Value.matchLink(fn: (Value, String, Value) -> R?): R? =
	fieldStack.linkOrNull?.run {
		value.sentenceOrNull?.let { sentence ->
			fn(stack.value, sentence.word, sentence.value)
		}
	}

fun <R : Any> Value.matchInfix(word: String, fn: (Value, Value) -> R?): R? =
	matchLink { lhs, linkWord, rhs ->
		ifOrNull(linkWord == word) {
			fn(lhs, rhs)
		}
	}

fun <R : Any> Value.matchPrefix(word: String, fn: (Value) -> R?): R? =
	matchInfix(word) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

fun <R : Any> Value.matchWord(fn: (String) -> R?): R? =
	matchLink { lhs, word, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				fn(word)
			}
		}
	}

fun <R : Any> Field.matchPrefix(word: String, fn: (Value) -> R?): R? =
	sentenceOrNull?.run {
		ifOrNull(this.word == word) {
			fn(value)
		}
	}

fun <R : Any> Field.matchWord(fn: (String) -> R?): R? =
	sentenceOrNull?.run {
		value.matchEmpty {
			fn(word)
		}
	}

fun <R : Any> Field.match(word: String, fn: () -> R?): R? =
	matchWord { aWord ->
		ifOrNull(aWord == word) {
			fn()
		}
	}

fun <R : Any> Field.matchLiteral(fn: (Literal) -> R?): R? =
	literalOrNull?.let(fn)

fun <R : Any> Field.matchText(fn: (String) -> R?): R? =
	matchLiteral { it.stringOrNull?.let(fn) }

fun <R : Any> Field.matchNumber(fn: (Number) -> R?): R? =
	matchLiteral { it.numberOrNull?.let(fn) }

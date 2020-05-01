package leo16

import leo.base.ifOrNull
import leo13.linkOrNull

fun <R : Any> Value.matchEmpty(fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

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

fun <R : Any> Value.matchLink(fn: (Value, String, Value) -> R?): R? =
	structOrNull?.lineStack?.linkOrNull?.run {
		fn(stack.struct.value, value.word, value.value)
	}

fun <R : Any> Value.matchWord(fn: (String) -> R?): R? =
	matchLink { lhs, word, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				fn(word)
			}
		}
	}

fun <R : Any> Line.matchPrefix(word: String, fn: (Value) -> R?): R? =
	ifOrNull(this.word == word) {
		fn(value)
	}

fun <R : Any> Line.matchWord(fn: (String) -> R?): R? =
	value.matchEmpty {
		fn(word)
	}

fun <R : Any> Line.match(word: String, fn: () -> R?): R? =
	this@match.matchWord { aWord ->
		ifOrNull(aWord == word) {
			fn()
		}
	}

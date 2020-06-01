package leo16

import leo.base.ifOrNull
import leo16.names.*
import java.math.BigDecimal

inline fun <R : Any> Value.matchEmpty(crossinline fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

inline fun <R : Any> Value.matchNative(fn: (Any?) -> R?): R? =
	theNativeOrNull?.run { fn(value) }

inline fun <R : Any> Value.matchText(crossinline fn: (String) -> R?): R? =
	matchPrefix(_text) { rhs ->
		rhs.matchNative { native ->
			(native as? String)?.let { string ->
				fn(string)
			}
		}
	}

inline fun <R : Any> Value.matchNumber(crossinline fn: (BigDecimal) -> R?): R? =
	matchPrefix(_number) { rhs ->
		rhs.matchNative { native ->
			(native as? BigDecimal)?.let { bigDecimal ->
				fn(bigDecimal)
			}
		}
	}

inline fun <R : Any> Value.matchLink(fn: (Value, String, Value) -> R?): R? =
	linkOrNull?.run {
		fn(previousValue, lastSentence.word, lastSentence.rhsValue)
	}

inline fun <R : Any> Value.split(fn: (Value, Sentence) -> R?): R? =
	linkOrNull?.run {
		fn(previousValue, lastSentence)
	}

inline fun <R : Any> Value.matchInfix(word: String, crossinline fn: (Value, Value) -> R?): R? =
	matchLink { lhs, linkWord, rhs ->
		ifOrNull(linkWord == word) {
			fn(lhs, rhs)
		}
	}

inline fun <R : Any> Value.matchPrefix(word: String, crossinline fn: (Value) -> R?): R? =
	matchInfix(word) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

inline fun <R : Any> Value.matchWord(crossinline fn: (String) -> R?): R? =
	matchLink { lhs, word, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				fn(word)
			}
		}
	}

inline fun <R : Any> Value.match(word: String, crossinline fn: () -> R?): R? =
	matchWord {
		ifOrNull(it == word) {
			fn()
		}
	}


inline fun <R : Any> Sentence.matchPrefix(word: String, crossinline fn: (Value) -> R?): R? =
	ifOrNull(this.word == word) {
		fn(rhsValue)
	}

inline fun <R : Any> Value.matchFunction(value: Value, crossinline fn: (Compiled) -> R?): R? =
	functionOrNull?.let { function ->
		ifOrNull(function.patternValue == value) {
			fn(function.compiled)
		}
	}

inline fun <R : Any> Sentence.matchWord(crossinline fn: (String) -> R?): R? =
	rhsValue.matchEmpty {
		fn(word)
	}

inline fun <R : Any> Sentence.match(word: String, crossinline fn: () -> R?): R? =
	matchWord { aWord ->
		ifOrNull(aWord == word) {
			fn()
		}
	}

inline fun <R : Any> Value.matchInfix(word: String, sentence: Sentence, crossinline fn: (Value, Value) -> R?): R? =
	sentence.matchPrefix(word) { rhs ->
		if (isEmpty) fn(rhs, this)
		else fn(this, rhs)
	}

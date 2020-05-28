package leo16

import leo.base.ifOrNull
import leo13.linkOrNull
import leo13.onlyOrNull
import leo16.names.*
import java.math.BigDecimal

inline fun <R : Any> Value.matchEmpty(crossinline fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

inline fun <R : Any> Value.matchNative(fn: (Any?) -> R?): R? =
	fieldStack.onlyOrNull?.matchNative(fn)

inline fun <R : Any> Value.matchText(crossinline fn: (String) -> R?): R? =
	fieldStack.onlyOrNull?.matchText(fn)

inline fun <R : Any> Value.matchNumber(crossinline fn: (BigDecimal) -> R?): R? =
	fieldStack.onlyOrNull?.matchNumber(fn)

inline fun <R : Any> Value.matchLink(fn: (Value, String, Value) -> R?): R? =
	fieldStack.linkOrNull?.run {
		value.sentenceOrNull?.let { sentence ->
			fn(stack.value, sentence.word, sentence.value)
		}
	}

inline fun <R : Any> Value.split(fn: (Value, Field) -> R?): R? =
	fieldStack.linkOrNull?.run {
		fn(stack.value, value)
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


inline fun <R : Any> Field.matchPrefix(word: String, crossinline fn: (Value) -> R?): R? =
	sentenceOrNull?.run {
		ifOrNull(this.word == word) {
			fn(value)
		}
	}

inline fun <R : Any> Field.matchFunction(value: Value, crossinline fn: (Compiled) -> R?): R? =
	functionOrNull?.let { taking ->
		ifOrNull(taking.patternValue == value) {
			fn(taking.compiled)
		}
	}

inline fun <R : Any> Value.matchFunction(value: Value, crossinline fn: (Compiled) -> R?): R? =
	onlyFieldOrNull?.matchFunction(value) { fn(it) }

inline fun <R : Any> Field.matchWord(crossinline fn: (String) -> R?): R? =
	sentenceOrNull?.run {
		value.matchEmpty {
			fn(word)
		}
	}

inline fun <R : Any> Field.match(word: String, crossinline fn: () -> R?): R? =
	matchWord { aWord ->
		ifOrNull(aWord == word) {
			fn()
		}
	}

inline fun <R : Any> Field.matchNative(fn: (Any?) -> R?): R? =
	theNativeOrNull?.let { fn(it.value) }

inline fun <R : Any> Field.matchText(crossinline fn: (String) -> R?): R? =
	matchPrefix(_text) { rhs ->
		rhs.matchNative { native ->
			(native as? String)?.let(fn)
		}
	}

inline fun <R : Any> Field.matchNumber(crossinline fn: (BigDecimal) -> R?): R? =
	matchPrefix(_number) { rhs ->
		rhs.matchNative { native ->
			(native as? BigDecimal)?.let(fn)
		}
	}

inline fun <R : Any> Value.matchInfix(word: String, field: Field, crossinline fn: (Value, Value) -> R?): R? =
	field.matchPrefix(word) { rhs ->
		if (isEmpty) fn(rhs, this)
		else fn(this, rhs)
	}

package leo16

import leo.base.ifOrNull
import leo13.Stack
import leo13.linkOrNull
import leo13.onlyOrNull
import leo15.numberName
import leo15.textName
import java.math.BigDecimal

fun <R : Any> Value.matchEmpty(fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

fun <R : Any> Value.matchNative(fn: (Any?) -> R?): R? =
	fieldStack.onlyOrNull?.matchNative(fn)

fun <R : Any> Value.matchText(fn: (String) -> R?): R? =
	fieldStack.onlyOrNull?.matchText(fn)

fun <R : Any> Value.matchNumber(fn: (BigDecimal) -> R?): R? =
	fieldStack.onlyOrNull?.matchNumber(fn)

fun <R : Any> Value.matchLink(fn: (Value, String, Value) -> R?): R? =
	fieldStack.linkOrNull?.run {
		value.sentenceOrNull?.let { sentence ->
			fn(stack.value, sentence.word, sentence.value)
		}
	}

fun <R : Any> Value.split(fn: (Value, Field) -> R?): R? =
	fieldStack.linkOrNull?.run {
		fn(stack.value, value)
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

fun <R : Any> Field.matchFunction(value: Value, fn: (Function) -> R?): R? =
	takingOrNull?.let { taking ->
		ifOrNull(taking.pattern == value.pattern) {
			fn(taking.function)
		}
	}

fun <R : Any> Value.matchFunction(value: Value, fn: (Function) -> R?): R? =
	onlyFieldOrNull?.matchFunction(value, fn)

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

fun <R : Any> Field.matchNative(fn: (Any?) -> R?): R? =
	theNativeOrNull?.let { fn(it.value) }

fun <R : Any> Field.matchText(fn: (String) -> R?): R? =
	matchPrefix(textName) { rhs ->
		rhs.matchNative { native ->
			(native as? String)?.let(fn)
		}
	}

fun <R : Any> Field.matchNumber(fn: (BigDecimal) -> R?): R? =
	matchPrefix(numberName) { rhs ->
		rhs.matchNative { native ->
			(native as? BigDecimal)?.let(fn)
		}
	}

fun <R : Any> Field.matchList(fn: (Stack<Value>) -> R?): R? =
	sentenceOrNull?.fieldListOrNull?.let(fn)

fun <R : Any> Value.matchList(fn: (Stack<Value>) -> R?): R? =
	fieldStack.onlyOrNull?.matchList(fn)

fun <R : Any> Field.matchStack(fn: (Stack<Value>) -> R?): R? =
	valueStackOrNull?.let(fn)

fun <R : Any> Value.matchStack(fn: (Stack<Value>) -> R?): R? =
	fieldStack.onlyOrNull?.matchStack(fn)

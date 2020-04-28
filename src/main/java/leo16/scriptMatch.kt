package leo16

import leo.base.ifOrNull
import leo13.linkOrNull

fun <R : Any> Script.matchEmpty(fn: () -> R?): R? =
	ifOrNull(isEmpty) { fn() }

fun <R : Any> Script.matchInfix(word: String, fn: (Script, Script) -> R?): R? =
	matchLink { lhs, linkWord, rhs ->
		ifOrNull(linkWord == word) {
			fn(lhs, rhs)
		}
	}

fun <R : Any> Script.matchPrefix(word: String, fn: (Script) -> R?): R? =
	matchInfix(word) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

fun <R : Any> Script.matchLink(fn: (Script, String, Script) -> R?): R? =
	sentenceStack.linkOrNull?.run {
		fn(stack.script, value.firstWord, value.followingScript)
	}

package leo.base

import leo.Term
import leo.backWord
import leo.term

object Back {
	override fun toString() = backWord.string
}

val back = Back

val backReflect: Term<Nothing>
	get() =
		backWord.term
package leo.base

import leo.Term
import leo.backWord
import leo.term

object Back

val goBack = Back

val backReflect: Term<Nothing>
	get() =
		backWord.term
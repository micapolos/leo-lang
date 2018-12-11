package leo.term

import leo.base.Stack

data class List<out V>(
	val termStack: Stack<Term<V>>)

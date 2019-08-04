package leo13

import leo9.Stack
import leo9.push

data class Index(val unitStack: Stack<Unit>)

val Stack<Unit>.index get() = Index(this)
val Index.inc get() = unitStack.push(Unit).index

package lambda.script

import leo.base.parenthesized
import leo.base.runIf

data class Application(val lhs: Term, val rhs: Term) {
	override fun toString() = "$lhsString($rhs)"
	private val lhsString = "$lhs".runIf(lhs is FunctionTerm) { parenthesized }
}

fun application(lhs: Term, rhs: Term) = Application(lhs, rhs)

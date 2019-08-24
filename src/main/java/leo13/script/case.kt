package leo13.script

import leo.base.notNullIf
import leo13.Script
import leo13.ScriptLine

data class Case(val name: String, val expr: Expr)

infix fun String.caseTo(expr: Expr) = Case(this, expr)

fun Case.evalOrNull(bindings: Bindings, lhs: ScriptLine): Script? =
	notNullIf(name == lhs.name) {
		lhs.rhs.eval(bindings, expr)
	}

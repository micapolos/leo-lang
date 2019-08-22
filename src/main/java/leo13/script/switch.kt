package leo13.script

import leo.base.notNullIf
import leo13.Script
import leo13.ScriptLine
import leo13.onlyLineOrNull
import leo9.Stack
import leo9.mapFirst

data class Switch(val caseStack: Stack<Case>)
data class Case(val name: String, val expr: Expr)

fun Switch.eval(bindings: Bindings, script: Script): Script =
	script.onlyLineOrNull!!.let { scriptLine ->
		caseStack.mapFirst {
			eval(bindings, scriptLine)
		}!!
	}

fun Case.eval(bindings: Bindings, scriptLine: ScriptLine): Script? =
	notNullIf(name == scriptLine.name) {
		expr.eval(bindings.push(scriptLine.rhs))
	}


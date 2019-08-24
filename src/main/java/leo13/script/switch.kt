package leo13.script

import leo13.Script
import leo13.linkOrNull
import leo9.Stack
import leo9.mapFirst
import leo9.stack

data class Switch(val caseStack: Stack<Case>)

fun switch(vararg cases: Case) = Switch(stack(*cases))

fun Script.eval(switch: Switch, bindings: Bindings) =
	switch.eval(bindings, this)

fun Switch.eval(bindings: Bindings, lhs: Script): Script =
	lhs.linkOrNull!!.line.let { line ->
		caseStack.mapFirst {
			evalOrNull(bindings, line)
		}!!
	}

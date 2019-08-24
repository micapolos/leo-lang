package leo13.script

import leo9.Stack
import leo9.stack

data class Switch(val caseStack: Stack<Case>)
fun switch(vararg cases: Case) = Switch(stack(*cases))

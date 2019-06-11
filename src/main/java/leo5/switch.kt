package leo5

import leo.base.Stack
import leo.base.fold
import leo.base.notNullAnd
import leo.base.stackOrNull

data class Switch(val caseStackOrNull: Stack<Case>?)

fun switch(vararg cases: Case) = Switch(stackOrNull(*cases))
fun Switch.contains(value: Value) =
	value.scriptOrNull?.applicationOrNull?.onlyLineOrNull.notNullAnd { line ->
		false.fold(caseStackOrNull) { case ->
			or(case.contains(line))
		}
	}

package leo13

import leo.base.ifOrNull
import leo9.*

data class ExprSwitch(val caseStack: Stack<ExprCase>)
data class ExprCase(val name: String, val expr: Expr)

val Stack<ExprCase>.exprSwitch get() = ExprSwitch(this)
fun exprSwitch() = ExprSwitch(stack())
fun ExprSwitch.plus(case: ExprCase) = caseStack.push(case).exprSwitch
fun switch(case: ExprCase, vararg cases: ExprCase) = nonEmptyStack(case, *cases).exprSwitch
infix fun String.caseTo(expr: Expr) = ExprCase(this, expr)

val TypeLine.exprSwitchOrNull: ExprSwitch?
	get() =
		ifOrNull(name == "switch") {
			rhs
				.choiceStack
				.mapOrNull { onlyLineOrNull?.exprCaseOrNull }
				?.exprSwitch
		}

val TypeLine.exprCaseOrNull: ExprCase?
	get() =
		ifOrNull(name == "case") {
			rhs.onlyLineOrNull?.let { line ->
				line.rhs.exprOrNull?.let { expr ->
					line.name caseTo expr
				}
			}
		}
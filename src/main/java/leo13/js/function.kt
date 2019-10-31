package leo13.js

import leo13.base.linesString

data class Function(val lhsType: Type, val rhsType: Type, val expression: Expression)

fun Function.code(index: Int) =
	linesString(
		"function fn$index(${argument.code}) {",
		"  ${expression.returnCode}",
		"}")

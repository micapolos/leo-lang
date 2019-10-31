package leo13.js

import leo13.base.linesString

data class Function(val lhsTypes: Types, val rhsTypes: Types, val expression: Expression)

fun Function.code(index: Int) =
	linesString(
		"function fn$index(${argument.code}) {",
		"  ${expression.returnCode}",
		"}")

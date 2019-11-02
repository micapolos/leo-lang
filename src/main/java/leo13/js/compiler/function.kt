package leo13.js.compiler

import leo13.base.linesString

data class Function(
	val parameterTypes: Types,
	val bodyTyped: Typed)

infix fun Types.gives(body: Typed) = Function(this, body)

fun Function.code(index: Int) =
	linesString(
		"function fn$index(${argument.code}) {",
		"  ${bodyTyped.expression.returnCode}",
		"}")

package leo13.js.compiler

import leo.base.assertEqualTo

fun <T> Compiler.assertResult(value: T) =
	result<T>().assertEqualTo(value)
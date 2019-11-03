package leo14

import leo.base.assertEqualTo

fun <T> Compiler.assertResult(value: T) =
	result<T>().assertEqualTo(value)
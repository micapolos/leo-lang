package leo15.lambda.runtime.java

import leo.base.assertEqualTo
import leo15.lambda.runtime.at
import leo15.lambda.runtime.lambda
import leo15.lambda.runtime.term
import leo15.lambda.runtime.value
import kotlin.test.Test

class CodeTest {
	@Test
	fun code() {
		PrintingJava("text")
			.code
			.assertEqualTo("fn { it.also { println(\"text: \$it\") } }")

		term(
			value(StringPlusStringJava),
			term(value("Hello, ".java)),
			term(value("world!".java)))
			.code
			.assertEqualTo("fn { x -> fn { y -> (x as String) + (y as String) } }.invoke(\"Hello, \").invoke(\"world!\")")

		term<Java>(lambda(at(0)))
			.code
			.assertEqualTo("fn { v0 -> v0 }")

		term<Java>(lambda(lambda(at(0))))
			.code
			.assertEqualTo("fn { v0 -> fn { v1 -> v1 } }")

		term<Java>(lambda(lambda(at(1))))
			.code
			.assertEqualTo("fn { v0 -> fn { v1 -> v0 } }")
	}
}
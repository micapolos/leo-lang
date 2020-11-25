package leo23.term.chez

import leo.base.assertEqualTo
import leo23.term.fn2
import leo23.term.get
import leo23.term.minus
import leo23.term.number
import leo23.term.textAppend
import leo23.term.text
import leo23.term.v0
import leo23.term.v1
import leo23.term.vector
import kotlin.test.Test

class StringTest {
	@Test
	fun stringAppend() {
		text("Hello, ")
			.textAppend(text("world!"))
			.string
			.assertEqualTo("(string-append \"Hello, \" \"world!\")")
	}

	@Test
	fun fn() {
		fn2(v1 - v0)
			.string
			.assertEqualTo("(lambda (v0 v1) (- v0 v1))")
	}

	@Test
	fun vectorAt() {
		vector(number(10), number(20))
			.get(number(0))
			.string
			.assertEqualTo("(vector-ref (vector 10 20) 0)")
	}
}
package leo21.native.chez

import leo.base.assertEqualTo
import leo21.native.fn2
import leo21.native.get
import leo21.native.minus
import leo21.native.number
import leo21.native.textAppend
import leo21.native.text
import leo21.native.v0
import leo21.native.v1
import leo21.native.vector
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
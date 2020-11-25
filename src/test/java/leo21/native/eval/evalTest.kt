package leo21.native.eval

import leo.base.assertEqualTo
import leo21.native.apply
import leo21.native.fn2
import leo21.native.get
import leo21.native.minus
import leo21.native.number
import leo21.native.text
import leo21.native.v0
import leo21.native.v1
import leo21.native.vector
import kotlin.test.Test

fun num(int: Int) = leo14.number(int)

class EvalTest {
	@Test
	fun vectorAt() {
		vector(text("Hello"), text("world"))
			.get(number(0))
			.eval
			.assertEqualTo("Hello")
	}

	@Test
	fun fnApply() {
		fn2(v1.minus(v0))
			.apply(number(5), number(3))
			.eval
			.assertEqualTo(num(2))
	}
}
package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.lambda.runtime.fn
import kotlin.test.Test

class ApplyTest {
	@Test
	fun normalization() {
		eval("x"(10), "y"(20), "point"()).assertEqualTo(eval("point"("x"(10), "y"(20))))
	}

	@Test
	fun ints() {
		eval("minus"(2)).assertEqualTo(-2)
		eval(2, "plus"(3)).assertEqualTo(5)
		eval(5, "minus"(3)).assertEqualTo(2)
		eval(2, "times"(3)).assertEqualTo(6)
	}

	@Test
	fun function() {
		eval(fn { (it as Int) * 2 }, 12).assertEqualTo(24)
	}
}
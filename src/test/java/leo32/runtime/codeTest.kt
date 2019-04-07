package leo32.runtime

import leo.base.assertEqualTo
import leo.base.string
import leo32.dsl.*
import kotlin.test.Test

class CodeTest {
	@Test
	fun string() {
		script().code.string.assertEqualTo("")

		script(one()).code.string.assertEqualTo("one")

		script(one(), two()).code.string.assertEqualTo("one\ntwo")

		script(circle(radius(float(10f)), center(x(float(12f)), y(float(15f)))))
			.code.string.assertEqualTo(
				"circle\n" +
				"  radius float 10.0\n" +
				"  center\n" +
				"    x float 12.0\n" +
				"    y float 15.0")
	}
}
package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script().code.assertEqualTo("")
		script("one" lineTo script()).code.assertEqualTo("one()")
		script("one" lineTo script(), "two" lineTo script()).code.assertEqualTo("one()two()")
		script("one" lineTo script("two" lineTo script())).code.assertEqualTo("one(two())")
	}

	@Test
	fun normalize() {
		script()
			.normalize
			.assertEqualTo(script())

		script("one" lineTo script())
			.normalize
			.assertEqualTo(script("one" lineTo script()))

		script("two" lineTo script("one" lineTo script()))
			.normalize
			.assertEqualTo(script("two" lineTo script("one" lineTo script())))

		script("one" lineTo script(), "two" lineTo script())
			.normalize
			.assertEqualTo(script("two" lineTo script("one" lineTo script())))

		script("one" lineTo script(), "two" lineTo script(), "three" lineTo script())
			.normalize
			.assertEqualTo(script("three" lineTo script("two" lineTo script("one" lineTo script()))))
	}
}
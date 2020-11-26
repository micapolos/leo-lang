package leo23.typed.term

import kotlin.test.Test

class DslTest {
	@Test
	fun struct() {
		"point" struct fields(
			"x" struct fields(number(10)),
			"y" struct fields(number(20)))
	}
}
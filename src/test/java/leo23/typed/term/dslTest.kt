package leo23.typed.term

import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		compiled(true)
		compiled(10)
		compiled(10.0)
		compiled("foo")

		"point" struct fields(
			"x" struct fields(compiled(10)),
			"y" struct fields(compiled(20)))
	}
}
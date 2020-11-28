package leo23.typed.term

import leo23.type.struct
import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		compiled(true)
		compiled(10)
		compiled(10.0)
		compiled("foo")

		"point" struct with(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20)))

		"bit" choice with(
			case("zero".structCompiled),
			case("one".struct))
	}
}
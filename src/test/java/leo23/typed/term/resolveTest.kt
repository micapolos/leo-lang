package leo23.typed.term

import leo.base.assertEqualTo
import kotlin.test.Test

class ResolveTest {
	@Test
	fun make() {
		compiled(
			"x" struct with(compiled(10)),
			"y" struct with(compiled(20)),
			"point".structCompiled)
			.resolveMakeOrNull
			.assertEqualTo(
				compiled(
					"x" struct with(compiled(10)),
					"y" struct with(compiled(20)))
					.make("point"))
	}

	@Test
	fun get() {
		compiled(
			"foo" struct with(
				compiled(10),
				compiled("text")),
			"number".structCompiled)
			.resolveGetOrNull
			.assertEqualTo(
				("foo" struct with(
					compiled(10),
					compiled("text")))
					.getOrNull("number")!!)
	}
}
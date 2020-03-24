package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	@Test
	fun apply_context() {
		val resolver = context()
			.push(
				rule(
					pattern(value("foo" lineTo value(), "bar" lineTo value())),
					body(thunk(value("zoo")))))
			.resolver(value("foo"))

		resolver
			.apply(line("bar"))
			.assertEqualTo(resolver.set(thunk(value("zoo"))))
	}

	@Test
	fun apply_definitions() {
		val resolver = context()
			.push(rule(pattern(value("foo")), body(thunk(value("bar")))))
			.resolver(value("zoo"))

		resolver
			.apply(givesName lineTo value("zar"))
			.assertEqualTo(
				resolver.compiler.push(
					definition(
						rule(
							pattern(value("zoo")),
							body(thunk(value("zar")))))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = context()
			.push(rule(pattern(value("foo")), body(thunk(value("bar")))))
			.resolver(value("zoo"))

		resolver
			.apply(plusName lineTo value("zar"))
			.assertEqualTo(resolver.set(thunk(value("zoo" lineTo value(), plusName lineTo value("zar")))))
	}
}
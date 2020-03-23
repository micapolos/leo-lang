package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	@Test
	fun apply_context() {
		val resolver = context()
			.push(
				rule(
					pattern(program("foo" valueTo program(), "bar" valueTo program())),
					body(thunk(program("zoo")))))
			.resolver(program("foo"))

		resolver
			.apply(value("bar"))
			.assertEqualTo(resolver.set(program("zoo")))
	}

	@Test
	fun apply_definitions() {
		val resolver = context()
			.push(rule(pattern(program("foo")), body(thunk(program("bar")))))
			.resolver(program("zoo"))

		resolver
			.apply(givesName valueTo program("zar"))
			.assertEqualTo(
				resolver.compiler.push(
					definition(
						rule(
							pattern(program("zoo")),
							body(thunk(program("zar")))))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = context()
			.push(rule(pattern(program("foo")), body(thunk(program("bar")))))
			.resolver(program("zoo"))

		resolver
			.apply(plusName valueTo program("zar"))
			.assertEqualTo(resolver.set(program("zoo" valueTo program(), plusName valueTo program("zar"))))
	}
}
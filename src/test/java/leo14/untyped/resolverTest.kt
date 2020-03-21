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
					body(constant(program("zoo")))))
			.resolver(program("foo"))

		resolver
			.apply(value("bar"))
			.assertEqualTo(resolver.set(program("zoo")))
	}

	@Test
	fun apply_definitions() {
		val resolver = context()
			.push(rule(pattern(program("foo")), body(constant(program("bar")))))
			.resolver(program("zoo"))

		resolver
			.apply("is" valueTo program("zar"))
			.assertEqualTo(
				resolver.context.push(
					rule(
						pattern(program("zoo")),
						body(constant(program("zar"))))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = context()
			.push(rule(pattern(program("foo")), body(constant(program("bar")))))
			.resolver(program("zoo"))

		resolver
			.apply("plus" valueTo program("zar"))
			.assertEqualTo(resolver.set(program("zoo" valueTo program(), "plus" valueTo program("zar"))))
	}
}
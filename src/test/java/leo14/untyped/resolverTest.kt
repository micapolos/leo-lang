package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	@Test
	fun apply_context() {
		val resolver = context()
			.push(pattern(program("foo" valueTo program(), "bar" valueTo program())) ruleTo body(program("zoo")))
			.resolver(program("foo"))

		resolver
			.apply(value("bar"))
			.assertEqualTo(resolver.set(program("zoo")))
	}

	@Test
	fun apply_definitions() {
		val resolver = context()
			.push(pattern(program("foo")) ruleTo body(program("bar")))
			.resolver(program("zoo"))

		resolver
			.apply("gives" valueTo program("zar"))
			.assertEqualTo(resolver.context.push(pattern(program("zoo")) ruleTo body(program("zar"))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = context()
			.push(pattern(program("foo")) ruleTo body(program("bar")))
			.resolver(program("zoo"))

		resolver
			.apply("zar" valueTo program())
			.assertEqualTo(resolver.set(program("zoo" valueTo program(), "zar" valueTo program())))
	}
}
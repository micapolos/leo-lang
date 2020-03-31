package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ResolverTest {
	@Test
	fun apply_scope() {
		val resolver = scope()
			.push(
				definition(
					binding(
						thunk(value("bar" lineTo value("foo" lineTo value()))),
						thunk(value("zoo")))))
			.resolver(thunk(value("foo")))

		resolver
			.apply(line("bar"))
			.assertEqualTo(resolver.set(thunk(value("zoo"))))
	}

	@Test
	fun apply_definitions() {
		val resolver = scope()
			.push(definition(binding(thunk(value("foo")), thunk(value("bar")))))
			.resolver(thunk(value("zoo")))

		resolver
			.apply(isName lineTo value("zar"))
			.assertEqualTo(
				resolver.scope.push(
					definition(
						binding(
							thunk(value("zoo")),
							thunk(value("zar"))))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = scope()
			.push(definition(binding(thunk(value("foo")), thunk(value("bar")))))
			.resolver(thunk(value("zoo")))

		resolver
			.apply(plusName lineTo value("zar"))
			.assertEqualTo(resolver.set(thunk(value("zoo" lineTo value(), plusName lineTo value("zar")))))
	}

	@Test
	fun writes() {
		scope()
			.resolver(thunk(value("defx")))
			.expands(
				script(
					"x" lineTo script(),
					"gives" lineTo script(literal(1))))
			.assertEqualTo(
				scope(
					rule(
						pattern(thunk(value("defx"))),
						expandsBody(
							script(
								"x" lineTo script(),
								"gives" lineTo script(literal(1))))))
					.resolver())
	}
}
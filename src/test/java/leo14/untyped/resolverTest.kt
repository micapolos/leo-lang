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
				rule(
					pattern(thunk(value("bar" lineTo value("foo" lineTo value())))),
					body(thunk(value("zoo")))))
			.resolver(thunk(value("foo")))

		resolver
			.apply(line("bar"))
			.assertEqualTo(resolver.set(thunk(value("zoo"))))
	}

	@Test
	fun apply_definitions() {
		val resolver = scope()
			.push(rule(pattern(thunk(value("foo"))), body(thunk(value("bar")))))
			.resolver(thunk(value("zoo")))

		resolver
			.apply(givesName lineTo value("zar"))
			.assertEqualTo(
				resolver.scope.push(
					definition(
						rule(
							pattern(thunk(value("zoo"))),
							body(thunk(value("zar")))))).resolver())
	}

	@Test
	fun apply_raw() {
		val resolver = scope()
			.push(rule(pattern(thunk(value("foo"))), body(thunk(value("bar")))))
			.resolver(thunk(value("zoo")))

		resolver
			.apply(plusName lineTo value("zar"))
			.assertEqualTo(resolver.set(thunk(value("zoo" lineTo value(), plusName lineTo value("zar")))))
	}

	@Test
	fun writes() {
		scope()
			.resolver(thunk(value("defx")))
			.writes(
				script(
					"x" lineTo script(),
					"gives" lineTo script(literal(1))))
			.assertEqualTo(
				scope(
					rule(
						pattern(thunk(value("defx"))),
						compileBody(
							script(
								"x" lineTo script(),
								"gives" lineTo script(literal(1))))))
					.resolver())
	}
}
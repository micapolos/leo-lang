package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ContextTest {
	private val context = context(
		rule(pattern(thunk(value("x"))), body(thunk(value("zero")))),
		rule(pattern(thunk(value("y"))), body(thunk(value("one")))),
		rule(pattern(thunk(value("x"))), body(thunk(value("two")))))

	@Test
	fun apply_rules() {
		context
			.apply(thunk(value("x")))
			.assertEqualTo(applied(thunk(value("two"))))

		context
			.apply(thunk(value("y")))
			.assertEqualTo(applied(thunk(value("one"))))

		context
			.apply(thunk(value("z")))
			.assertEqualTo(null)
	}

	@Test
	fun compile_is() {
		context
			.compile(
				thunk(
					value(
						"foo" lineTo value(),
						givesName lineTo value("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(thunk(value("foo"))),
						body(thunk(value("bar"))))))
	}

	@Test
	fun compile_does() {
		context
			.compile(
				thunk(
					value(
						"foo" lineTo value(),
						doesName lineTo value("bar"))))
			.assertEqualTo(
				context.push(
					rule(
						pattern(thunk(value("foo"))),
						evalBody(script("bar")))))
	}

	@Test
	fun applyCompile() {
		val context = context(
			rule(
				pattern(thunk(value("defx"))),
				compileBody(
					script(
						"quote" lineTo script(
							"x" lineTo script(),
							"gives" lineTo script(literal(1)))))))

		context
			.apply(thunk(value("defx")))
			.assertEqualTo(
				applied(
					script(
						"x" lineTo script(),
						"gives" lineTo script(literal(1)))))
	}
}
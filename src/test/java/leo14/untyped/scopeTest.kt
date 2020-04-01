package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScopeTest {
	private val scope = scope(
		definition(binding(thunk(value("x")), thunk(value("zero")))),
		definition(binding(thunk(value("y")), thunk(value("one")))),
		definition(binding(thunk(value("x")), thunk(value("two")))))

	@Test
	fun apply_rules() {
		scope
			.apply(thunk(value("x")))
			.assertEqualTo(applied(thunk(value("two"))))

		scope
			.apply(thunk(value("y")))
			.assertEqualTo(applied(thunk(value("one"))))

		scope
			.apply(thunk(value("z")))
			.assertEqualTo(null)
	}

	@Test
	fun compile_is() {
		scope
			.compile(
				thunk(
					value(
						"foo" lineTo value(),
						isName lineTo value("bar"))))
			.assertEqualTo(
				scope.push(
					definition(
						binding(
							thunk(value("foo")),
							thunk(value("bar"))))))
	}

	@Test
	fun applyCompile() {
		val scope = scope(
			rule(
				pattern(thunk(value("defx"))),
				expandsBody(
					script(
						"quote" lineTo script(
							"x" lineTo script(),
							"gives" lineTo script(literal(1)))))))

		scope
			.apply(thunk(value("defx")))
			.assertEqualTo(
				applied(
					script(
						"x" lineTo script(),
						"gives" lineTo script(literal(1)))))
	}
}
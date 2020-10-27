package leo20

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScopeTest {
	@Test
	fun value_empty() {
		emptyScope
			.value(script())
			.assertEqualTo(value())
	}

	@Test
	fun value_string() {
		emptyScope
			.value(script(literal("foo")))
			.assertEqualTo(value(line("foo")))
	}

	@Test
	fun value_number() {
		emptyScope
			.value(script(literal(10)))
			.assertEqualTo(value(line(10)))
	}

	@Test
	fun value_struct() {
		emptyScope
			.value(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20)))))
			.assertEqualTo(
				value(
					"point" lineTo value(
						"x" lineTo value(line(10)),
						"y" lineTo value(line(20)))))
	}

	@Test
	fun value_get() {
		emptyScope
			.value(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20))),
					"x" lineTo script()))
			.assertEqualTo(
				value(
					"x" lineTo value(line(10))))
	}

	@Test
	fun value_make() {
		emptyScope
			.value(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"point" lineTo script()))
			.assertEqualTo(
				value(
					"point" lineTo value(
						"x" lineTo value(line(10)),
						"y" lineTo value(line(20)))))
	}

	@Test
	fun value_function() {
		emptyScope
			.value(script("function" lineTo script("foo")))
			.assertEqualTo(value(line(emptyScope.function(body(script("foo"))))))
	}

	@Test
	fun value_apply() {
		emptyScope
			.value(
				script(
					"function" lineTo script("number"),
					"apply" lineTo script(literal(10))))
			.assertEqualTo(value(line(10)))
	}

	@Test
	fun value_do() {
		emptyScope
			.value(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"do" lineTo script("x")))
			.assertEqualTo(value("x" lineTo value(line(10))))
	}

	@Test
	fun value_switch() {
		emptyScope
			.value(
				script(
					"shape" lineTo script(
						"circle" lineTo script(
							"radius" lineTo script(literal(10)))),
					"switch" lineTo script(
						"circle" lineTo script("radius"),
						"square" lineTo script("side"))))
			.assertEqualTo(value("radius" lineTo value(line(10))))
	}

	@Test
	fun value_defineGives() {
		emptyScope
			.value(
				script(
					"define" lineTo script(
						"number" lineTo script("any"),
						"gives" lineTo script(literal("ok")))))
			.assertEqualTo(value())
	}

	@Test
	fun define_gives() {
		emptyScope
			.defineOrNull(
				script(
					"number" lineTo script("any"),
					"gives" lineTo script("number")))
			.assertEqualTo(
				emptyScope
					.push(Binding(
						pattern("number" lineTo anyPattern),
						value("number" lineTo value()),
						false)))
	}

	@Test
	fun define_does() {
		emptyScope
			.defineOrNull(
				script(
					"number" lineTo script("any"),
					"does" lineTo script("number")))
			.assertEqualTo(
				emptyScope
					.push(Binding(
						pattern("number" lineTo anyPattern),
						value(line(emptyScope.function(body(script("number"))))),
						true)))
	}

	@Test
	fun resolveGives() {
		emptyScope
			.push(
				Binding(
					pattern("number" lineTo anyPattern),
					value("ok" lineTo value()),
					false))
			.resolveOrNull(value(line(128)))
			.assertEqualTo(value("ok" lineTo value()))
	}

	@Test
	fun resolveDoes() {
		emptyScope
			.push(
				Binding(
					pattern("number" lineTo anyPattern),
					value(line(emptyScope.function(body(script("good" lineTo script("number")))))),
					true))
			.resolveOrNull(value(line(128)))
			.assertEqualTo(value("good" lineTo value(line(128))))
	}

	@Test
	fun resolveNumberPlus() {
		emptyScope
			.push(
				Binding(
					pattern(
						numberPatternLine,
						"plus" lineTo pattern(numberPatternLine)),
					value(line(emptyScope.function(NumberPlusBody))),
					true))
			.resolveOrNull(value(line(128), "plus" lineTo value(line(128))))
			.assertEqualTo(value(line(256)))
	}

	@Test
	fun value_defineDoes() {
		emptyScope
			.value(
				script(
					"define" lineTo script(
						"number" lineTo script("any"),
						"does" lineTo script("number"))))
			.assertEqualTo(value())
	}

	@Test
	fun value_defineDoesResolve() {
		emptyScope
			.value(
				script(
					"define" lineTo script(
						"number" lineTo script("any"),
						"does" lineTo script(
							"good" lineTo script("number"))),
					leo14.line(literal(128))))
			.assertEqualTo(value("good" lineTo value(line(128))))
	}

	@Test
	fun value_numberPlus() {
		emptyScope
			.pushPrelude
			.value(
				script(
					leo14.line(literal(2)),
					"plus" lineTo script(leo14.line(literal(3)))))
			.assertEqualTo(value(line(5)))
	}


	@Test
	fun value_numberEquals() {
		emptyScope
			.pushPrelude
			.value(
				script(
					leo14.line(literal(2)),
					"equals" lineTo script(leo14.line(literal(3)))))
			.assertEqualTo(false.value)
	}
}
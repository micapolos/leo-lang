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
					.push(ValueBinding(
						pattern("number" lineTo anyPattern),
						value("number" lineTo value()))))
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
					.push(FunctionBinding(
						pattern("number" lineTo anyPattern),
						emptyScope.function(body(script("number"))))))
	}

	@Test
	fun resolveGives() {
		emptyScope
			.push(
				ValueBinding(
					pattern("number" lineTo anyPattern),
					value("ok" lineTo value())))
			.resolveOrNull(value(line(128)))
			.assertEqualTo(value("ok" lineTo value()))
	}

	@Test
	fun resolveDoes() {
		emptyScope
			.push(
				FunctionBinding(
					pattern("number" lineTo anyPattern),
					emptyScope.function(body(script("good" lineTo script("number"))))))
			.resolveOrNull(value(line(128)))
			.assertEqualTo(value("good" lineTo value(line(128))))
	}

	@Test
	fun resolveNumberPlus() {
		emptyScope
			.push(
				FunctionBinding(
					pattern(
						numberPatternLine,
						"plus" lineTo pattern(numberPatternLine)),
					emptyScope.function(NumberPlusBody)))
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

	@Test
	fun value_doRecursively() {
		emptyScope
			.value(
				script(
					"bit" lineTo script("one"),
					"do" lineTo script(
						"recursively" lineTo script(
							"bit" lineTo script(),
							"switch" lineTo script(
								"one" lineTo script(
									"bit" lineTo script("zero"),
									"recurse" lineTo script()),
								"zero" lineTo script("done"))))))
			.assertEqualTo(value("done" lineTo value()))
	}

	@Test
	fun value_doFactorial() {
		emptyScope
			.pushPrelude
			.value(
				script(
					"sum" lineTo script(literal(10)),
					"do" lineTo script(
						"recursively" lineTo script(
							"sum" lineTo script(),
							"number" lineTo script(),
							"equals" lineTo script(literal(0)),
							"switch" lineTo script(
								"true" lineTo script(literal(0)),
								"false" lineTo script(
									"sum" lineTo script(),
									"number" lineTo script(),
									"plus" lineTo script(
										"sum" lineTo script(),
										"number" lineTo script(),
										"minus" lineTo script(literal(1)),
										"sum" lineTo script(),
										"recurse" lineTo script())))))))
			.assertEqualTo(value(line(55)))
	}
}
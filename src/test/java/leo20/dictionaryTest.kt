package leo20

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test
import kotlin.test.assertFails

class DictionaryTest {
	@Test
	fun value_empty() {
		emptyDictionary
			.value(script())
			.assertEqualTo(value())
	}

	@Test
	fun value_string() {
		emptyDictionary
			.value(script(literal("foo")))
			.assertEqualTo(value(line("foo")))
	}

	@Test
	fun value_number() {
		emptyDictionary
			.value(script(literal(10)))
			.assertEqualTo(value(line(10)))
	}

	@Test
	fun value_struct() {
		emptyDictionary
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
	fun value_staticGet() {
		emptyDictionary
			.value(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"z" lineTo script("get" lineTo script("x" lineTo script("number")))))
			.assertEqualTo(
				value(
					"x" lineTo value(line(10)),
					"y" lineTo value(line(20)),
					"z" lineTo value(line(10))))
	}

	@Test
	fun value_get() {
		emptyDictionary
			.value(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20))),
					"get" lineTo script(
						"x" lineTo script())))
			.assertEqualTo(
				value(
					"x" lineTo value(line(10))))
	}

	@Test
	fun value_getDeep() {
		emptyDictionary
			.value(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20))),
					"get" lineTo script(
						"x" lineTo script(
							"number" lineTo script()))))
			.assertEqualTo(
				value(line(10)))
	}

	@Test
	fun value_make() {
		emptyDictionary
			.value(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"make" lineTo script("point" lineTo script())))
			.assertEqualTo(
				value(
					"point" lineTo value(
						"x" lineTo value(line(10)),
						"y" lineTo value(line(20)))))
	}

	@Test
	fun value_function() {
		emptyDictionary
			.value(script("function" lineTo script("foo")))
			.assertEqualTo(value(line(emptyDictionary.function(body(script("foo"))))))
	}

	@Test
	fun value_apply() {
		emptyDictionary
			.value(
				script(
					"function" lineTo script("get" lineTo script("number")),
					"apply" lineTo script(literal(10))))
			.assertEqualTo(value(line(10)))
	}

	@Test
	fun value_do() {
		emptyDictionary
			.value(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"do" lineTo script("get" lineTo script("x"))))
			.assertEqualTo(value("x" lineTo value(line(10))))
	}

	@Test
	fun value_switch() {
		emptyDictionary
			.value(
				script(
					"shape" lineTo script(
						"circle" lineTo script(
							"radius" lineTo script(literal(10)))),
					"switch" lineTo script(
						"circle" lineTo script("does" lineTo script("get" lineTo script("circle" lineTo script("radius")))),
						"square" lineTo script("does" lineTo script("get" lineTo script("square" lineTo script("side")))))))
			.assertEqualTo(value("radius" lineTo value(line(10))))
	}

	@Test
	fun define_does() {
		emptyDictionary
			.defineOrNull(
				script(
					"number" lineTo script("any"),
					"does" lineTo script("number")))
			.assertEqualTo(
				emptyDictionary
					.push(FunctionDefinition(
						pattern("number" lineTo anyPattern),
						emptyDictionary.function(body(script("number"))),
						isRecursive = false)))
	}

	@Test
	fun define_does_recursively() {
		emptyDictionary
			.defineOrNull(
				script(
					"number" lineTo script("any"),
					"does" lineTo script(
						"recursively" lineTo script("number"))))
			.assertEqualTo(
				emptyDictionary
					.push(FunctionDefinition(
						pattern("number" lineTo anyPattern),
						emptyDictionary.function(body(script("number"))),
						isRecursive = true)))
	}

	@Test
	fun resolveDoes() {
		emptyDictionary
			.push(
				FunctionDefinition(
					pattern("number" lineTo anyPattern),
					emptyDictionary.function(body(script("get" lineTo script("number")))),
					isRecursive = false))
			.resolveOrNull(value(line(128)))
			.assertEqualTo(value(line(128)))
	}

	@Test
	fun resolveNumberPlus() {
		emptyDictionary
			.push(
				FunctionDefinition(
					pattern(
						numberPatternLine,
						"plus" lineTo pattern(numberPatternLine)),
					emptyDictionary.function(NumberPlusBody),
					isRecursive = false))
			.resolveOrNull(value(line(128), "plus" lineTo value(line(128))))
			.assertEqualTo(value(line(256)))
	}

	@Test
	fun value_defineDoes() {
		emptyDictionary
			.value(
				script(
					"define" lineTo script(
						"number" lineTo script("any"),
						"does" lineTo script("number"))))
			.assertEqualTo(value())
	}

	@Test
	fun value_defineDoesResolve() {
		emptyDictionary
			.value(
				script(
					"define" lineTo script(
						"number" lineTo script("any"),
						"does" lineTo script(
							"get" lineTo script("number"))),
					leo14.line(literal(128))))
			.assertEqualTo(value(line(128)))
	}

	@Test
	fun value_numberPlus() {
		emptyDictionary
			.pushPrelude
			.value(
				script(
					leo14.line(literal(2)),
					"plus" lineTo script(leo14.line(literal(3)))))
			.assertEqualTo(value(line(5)))
	}

	@Test
	fun value_numberEquals() {
		emptyDictionary
			.pushPrelude
			.value(
				script(
					leo14.line(literal(2)),
					"equals" lineTo script(leo14.line(literal(3)))))
			.assertEqualTo(false.value)
	}

	@Test
	fun value_resolveRecursively() {
		emptyDictionary
			.push(
				FunctionDefinition(
					pattern(numberPatternLine, "sum" lineTo pattern()),
					emptyDictionary.pushPrelude.function(
						body(
							script(
								"get" lineTo script("number"),
								"equals" lineTo script(literal(1)),
								"switch" lineTo script(
									"true" lineTo script(
										"does" lineTo script(literal(1))),
									"false" lineTo script(
										"does" lineTo script(
											"get" lineTo script("number"),
											"plus" lineTo script(
												"get" lineTo script("number"),
												"minus" lineTo script(literal(1)),
												"sum" lineTo script()))))))),
					isRecursive = true))
			.value(
				script(
					leo14.line(literal(10)),
					"sum" lineTo script()))
			.assertEqualTo(value(line(55)))
	}

	@Test
	fun test_failure() {
		assertFails {
			emptyDictionary
				.pushPrelude
				.value(
					script(
						"test" lineTo script(
							leo14.line(literal(2)),
							"plus" lineTo script(literal(3)),
							"equals" lineTo script(
								leo14.line(literal(3)),
								"plus" lineTo script(literal(4))))))
		}
	}

	@Test
	fun test_pass() {
		emptyDictionary
			.pushPrelude
			.value(
				script(
					"test" lineTo script(
						leo14.line(literal(2)),
						"plus" lineTo script(literal(3)),
						"equals" lineTo script(
							leo14.line(literal(3)),
							"plus" lineTo script(literal(2))))))
			.assertEqualTo(value())
	}

	@Test
	fun testFails_success() {
		emptyDictionary
			.value(
				script(
					"test" lineTo script(
						"fail" lineTo script(),
						"fails" lineTo script())))
			.assertEqualTo(value())
	}

	@Test
	fun testFails_failure() {
		assertFails {
			emptyDictionary
				.value(
					script(
						"test" lineTo script(
							"zero" lineTo script(),
							"fails" lineTo script())))
		}
	}
}
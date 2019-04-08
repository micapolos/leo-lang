package leo32.runtime

import leo.base.assertEqualTo
import org.junit.Test

class EvaluateTest {
	@Test
	fun empty() {
		script().evaluate.assertEqualTo(script())
	}

	@Test
	fun one() {
		script("one").evaluate.assertEqualTo(script("one"))
	}

	@Test
	fun one__two() {
		script(
			"one" to script(),
			"two" to script())
			.evaluate
			.assertEqualTo(script("two" to script("one")))
	}

	@Test
	fun one_two() {
		script("one" to script("two"))
			.evaluate
			.assertEqualTo(script("one" to script("two")))
	}

	@Test
	fun one_two___three() {
		script(
			"one" to script("two"),
			"three" to script())
			.evaluate
			.assertEqualTo(
				script(
					"three" to script(
						"one" to script("two"))))
	}

	@Test
	fun one_two___three_four() {
		script(
			"one" to script("two"),
			"three" to script("four"))
			.evaluate
			.assertEqualTo(
				script(
					"one" to script("two"),
					"three" to script("four")))
	}

	@Test
	fun one__one() {
		script(
			"one" to script(),
			"one" to script())
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun one_two___one() {
		script(
			"one" to script("two"),
			"one" to script())
			.evaluate
			.assertEqualTo(script("two"))
	}

	@Test
	fun one_two___two_three___one() {
		script(
			"one" to script("two"),
			"two" to script("three"),
			"one" to script())
			.evaluate
			.assertEqualTo(script("two"))
	}

	@Test
	fun one_two___two_three___two() {
		script(
			"one" to script("two"),
			"two" to script("three"),
			"two" to script())
			.evaluate
			.assertEqualTo(script("three"))
	}

	@Test
	fun one_two___two_three___three() {
		script(
			"one" to script("two"),
			"two" to script("three"),
			"three" to script())
			.evaluate
			.assertEqualTo(
				script(
					"three" to script(
						"one" to script("two"),
						"two" to script("three"))))
	}

	@Test
	fun equals() {
		script("equals")
			.evaluate
			.assertEqualTo(script("boolean" to script("true")))
	}

	@Test
	fun one__equals_one() {
		script(
			"one" to script(),
			"equals" to script("one"))
			.evaluate
			.assertEqualTo(script("boolean" to script("true")))
	}

	@Test
	fun one__equals_two() {
		script(
			"one" to script(),
			"equals" to script("two"))
			.evaluate
			.assertEqualTo(script("boolean" to script("false")))
	}

	@Test
	fun it_one__equals_two() {
		script(
			"it" to script(
				"one" to script(),
				"equals" to script("two")))
			.evaluate
			.assertEqualTo(script("it" to script("boolean" to script("false"))))
	}

	@Test
	fun quote_one__equals_two() {
		script(
			"quote" to script(
				"one" to script(),
				"equals" to script("two")))
			.evaluate
			.assertEqualTo(
				script(
					"one" to script(),
					"equals" to script("two")))
	}

	@Test
	fun quote_one__equals_two___evaluate() {
		script(
			"quote" to script(
				"one" to script(),
				"equals" to script("two")),
			"unquote" to script())
			.evaluate
			.assertEqualTo(
				script("boolean" to script("false")))
	}
}

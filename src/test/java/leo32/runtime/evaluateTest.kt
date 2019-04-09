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
	fun quote() {
		script("quote")
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun quote_one() {
		script("quote" to script("one"))
			.evaluate
			.assertEqualTo(script("one"))
	}

	@Test
	fun quote_one__two() {
		script("quote" to script(
			"one" to script(),
			"two" to script()))
			.evaluate
			.assertEqualTo(
				script(
					"one" to script(),
					"two" to script()))
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

	@Test
	fun it_quote() {
		script("it" to script("quote"))
			.evaluate
			.assertEqualTo(script("it"))
	}

	@Test
	fun it_quote_one() {
		script("it" to script("quote" to script("one")))
			.evaluate
			.assertEqualTo(script("it" to script("one")))
	}

	@Test
	fun quote_quote() {
		script("quote" to script("quote"))
			.evaluate
			.assertEqualTo(script("quote"))
	}

	@Test
	fun error() {
		script("error")
			.evaluate
			.assertEqualTo(script("error"))
	}

	@Test
	fun error_one() {
		script("error" to script("one"))
			.evaluate
			.assertEqualTo(script("error" to script("one")))
	}

	@Test
	fun error__one() {
		script(
			"error" to script(),
			"one" to script())
			.evaluate
			.assertEqualTo(script("error" to script("one")))
	}

	@Test
	fun error__one__two() {
		script(
			"error" to script(),
			"one" to script(),
			"two" to script())
			.evaluate
			.assertEqualTo(
				script(
					"error" to script(
						"one" to script(),
						"two" to script())))
	}

	@Test
	fun error__one__equals_one() {
		script(
			"error" to script(),
			"one" to script(),
			"equals" to script("one"))
			.evaluate
			.assertEqualTo(
				script(
					"error" to script(
						"one" to script(),
						"equals" to script("one"))))
	}

	@Test
	fun classify() {
		script("classify" to script())
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun either_zero___classify() {
		script(
			"either" to script("zero"),
			"classify" to script())
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun either_zero___either_one___classify() {
		script(
			"either" to script("zero"),
			"either" to script("one"),
			"classify" to script())
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun zero__gives_one() {
		script(
			"zero" to script(),
			"gives" to script("one"))
			.evaluate
			.assertEqualTo(script())
	}

//	@Test
//	fun zero__gives_one___zero() {
//		script(
//			"zero" to script(),
//			"gives" to script("one"),
//			"zero" to script())
//			.evaluate
//			.assertEqualTo(script("one"))
//	}

//	@Test
//	fun either_zero___classify__zero__class() {
//		script(
//			"either" to script("zero"),
//			"classify" to script(),
//			"zero" to script(),
//			"class" to script())
//			.evaluate
//			.assertEqualTo(script())
//	}
//
//	@Test
//	fun either_zero___either_one___classify__zero__class() {
//		script(
//			"either" to script("zero"),
//			"either" to script("one"),
//			"classify" to script(),
//			"class" to script("zero"))
//			.evaluate
//			.assertEqualTo(script())
//	}
//
//	@Test
//	fun either_zero___either_one___classify__one__class() {
//		script(
//			"either" to script("zero"),
//			"either" to script("one"),
//			"classify" to script(),
//			"one" to script(),
//			"class" to script())
//			.evaluate
//			.assertEqualTo(script())
//	}
}

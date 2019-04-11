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
	fun one_foo___one_bar() {
		script(
			"one" to script("foo"),
			"one" to script("bar"))
			.evaluate
			.assertEqualTo(
				script(
					"one" to script("foo"),
					"one" to script("bar")))
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
	fun error_one__two() {
		script(
			"error" to script("one"),
			"two" to script())
			.evaluate
			.assertEqualTo(script("error" to script("one")))
	}

	@Test
	fun foo__has_bar() {
		script(
			"foo" to script(),
			"has" to script("bar"))
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun foo__has_bar__foo() {
		script(
			"foo" to script(),
			"has" to script("bar"),
			"foo" to script())
			.evaluate
			.assertEqualTo(script("foo"))
	}

	@Test
	fun foo__has_bar__class_foo_bar() {
		script(
			"foo" to script(),
			"has" to script("bar"),
			"class" to script("foo" to script("bar" to script())))
			.evaluate
			.assertEqualTo(script("foo"))
	}

	@Test
	fun foo__has_bar__describe_foo() {
		script(
			"foo" to script(),
			"has" to script("bar"),
			"describe" to script("foo"))
			.evaluate
			.assertEqualTo(script("foo" to script("bar")))
	}

	@Test
	fun foo_zoo___has_bar__foo_zoo() {
		script(
			"foo" to script("zoo" to script()),
			"has" to script("bar"),
			"foo" to script("zoo"))
			.evaluate
			.assertEqualTo(script("foo" to script("zoo")))
	}

	@Test
	fun foo_zoo___has_bar__class_foo_zoo_bar() {
		script(
			"foo" to script("zoo" to script()),
			"has" to script("bar"),
			"class" to script("foo" to script("zoo" to script("bar"))))
			.evaluate
			.assertEqualTo(script("foo" to script("zoo")))
	}

	@Test
	fun foo_zoo___has_bar__describe_foo_zoo() {
		script(
			"foo" to script("zoo" to script()),
			"has" to script("bar"),
			"describe" to script("foo" to script("zoo")))
			.evaluate
			.assertEqualTo(script("foo" to script("zoo" to script("bar"))))
	}

	@Test
	fun bit__has_either_zero___either_one___bit() {
		script(
			"bit" to script(),
			"has" to script(
				"either" to script("zero"),
				"either" to script("one")),
			"bit" to script())
			.evaluate
			.assertEqualTo(script("bit"))
	}

	@Test
	fun bit__has_either_zero___either_one____class_bit_zero() {
		script(
			"bit" to script(),
			"has" to script(
				"either" to script("zero"),
				"either" to script("one")),
			"class" to script("bit" to script("zero")))
			.evaluate
			.assertEqualTo(script("bit"))
	}

	@Test
	fun bit__has_either_zero___either_one____describe_bit() {
		script(
			"bit" to script(),
			"has" to script(
				"either" to script("zero"),
				"either" to script("one")),
			"describe" to script("bit"))
			.evaluate
			.assertEqualTo(
				script("bit" to script(
					"either" to script("zero"),
					"either" to script("one"))))
	}

	@Test
	fun bit__has_either_zero___either_one____class_bit_one() {
		script(
			"bit" to script(),
			"has" to script(
				"either" to script("zero"),
				"either" to script("one")),
			"class" to script("bit" to script("one")))
			.evaluate
			.assertEqualTo(script("bit"))
	}

	@Test
	fun zero__gives_one() {
		script(
			"zero" to script(),
			"gives" to script("one"))
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun zero__gives_one___zero() {
		script(
			"zero" to script(),
			"gives" to script("one"),
			"zero" to script())
			.evaluate
			.assertEqualTo(script("one"))
	}
}

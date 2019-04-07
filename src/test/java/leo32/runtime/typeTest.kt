package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun typeTerm() {
		type("bit" to type(either("zero"), either("one")))
			.term
			.assertEqualTo(
				term(
					"bit" to term(
						"either" to term("zero"),
						"either" to term("one"))))
	}

	@Test
	fun parseType() {
		term()
			.parseType
			.assertEqualTo(type())

		term("one")
			.parseType
			.assertEqualTo(type("one"))

		term("one" to term("two"))
			.parseType
			.assertEqualTo(type("one" to type("two")))

		term("either" to term("zero"))
			.parseType
			.assertEqualTo(type("zero"))

		term(
			"either" to term("zero"),
			"either" to term("one"))
			.parseType
			.assertEqualTo(type(either("zero"), either("one")))

		term(
			"either" to term("zero"),
			"either" to term("one"),
			"plus" to term("two"))
			.parseType
			.assertEqualTo(
				type(
					"either" to type("zero"),
					"either" to type("one"),
					"plus" to type("two")))
	}
}
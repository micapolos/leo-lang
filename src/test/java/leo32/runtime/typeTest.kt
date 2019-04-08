package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun rawType() {
		term()
			.rawType
			.assertEqualTo(type())

		term("zero")
			.rawType
			.assertEqualTo(type("zero"))

		term("either" to term("zero"))
			.rawType
			.assertEqualTo(type("either" to type("zero")))

		term("either" to term("zero"), "either" to term("one"))
			.rawType
			.assertEqualTo(type("either" to type("zero"), "either" to type("one")))
	}

	@Test
	fun parseType() {
		term()
			.parseType
			.assertEqualTo(type())

		term("one")
			.parseType
			.assertEqualTo(type("one"))

		term("either" to term("zero"))
			.parseType
			.assertEqualTo(type(either("zero")))

		term("either" to term("zero"), "either" to term("one"))
			.parseType
			.assertEqualTo(type(either("zero"), either("one")))
	}
}
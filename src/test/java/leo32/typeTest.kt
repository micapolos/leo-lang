package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun bitTypeScript() {
		type(
			field("bit", type(
				choice(field("zero")),
				choice(field("one")))))
			.script
			.string
			.assertEqualTo("bit either zero   either one    ")
	}

	@Test
	fun escapeQuote() {
		type(field("quote", type(field("foo"))))
			.script
			.string
			.assertEqualTo("quote quote foo    ")
	}

	@Test
	fun escapeEither() {
		type(field("either", type(field("foo"))))
			.script
			.string
			.assertEqualTo("quote either foo    ")
	}
}
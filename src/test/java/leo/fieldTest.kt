package leo

import leo.base.assertEqualTo
import leo.base.nullOf
import leo.base.string
import kotlin.test.Test

class FieldTest {
	@Test
	fun string() {
		numberWord.fieldTo(term(1))
			.string
			.assertEqualTo("number 1")
	}

	@Test
	fun orNullReflect_null() {
		nullOf<Letter>()
			.orNullReflect(letterWord, Letter::reflect)
			.string
			.assertEqualTo("letter null")
	}

	@Test
	fun orNullReflect_notNull() {
		Letter.A
			.orNullReflect(letterWord, Letter::reflect)
			.string
			.assertEqualTo("letter a")
	}
}
package leo.lab

import leo.Letter
import leo.base.assertEqualTo
import leo.base.nullOf
import leo.base.string
import leo.letterWord
import leo.numberWord
import leo.oneWord
import kotlin.test.Test

class FieldTest {
	@Test
	fun string() {
		numberWord.fieldTo(1.metaTerm)
			.string
			.assertEqualTo("number Integer(1)")
	}

	@Test
	fun coreString() {
		numberWord
			.fieldTo(oneWord.term)
			.term
			.coreString
			.assertEqualTo("number(one())")
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
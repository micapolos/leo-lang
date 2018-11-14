package leo

import leo.base.*
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

	@Test
	fun byteStream() {
		oneWord
			.fieldTo(term<Value>(twoWord))
			.byteStream { fail }
			.assertContains(
				Letter.O.byte,
				Letter.N.byte,
				Letter.E.byte,
				'('.toByte(),
				Letter.T.byte,
				Letter.W.byte,
				Letter.O.byte,
				')'.toByte())
	}
}
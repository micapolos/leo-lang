package leo.lab

import leo.*
import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.nullOf
import leo.base.string
import kotlin.test.Test

class FieldTest {
	@Test
	fun string() {
		numberWord.fieldTo(1.metaTerm)
			.string
			.assertEqualTo("number Integer(1)")
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
		oneWord.fieldTo(twoWord.term)
			.byteStream
			.assertContains(
				Letter.O.byte,
				Letter.N.byte,
				Letter.E.byte,
				'('.toByte(),
				Letter.T.byte,
				Letter.W.byte,
				Letter.O.byte,
				'('.toByte(),
				')'.toByte(),
				')'.toByte())
	}
}
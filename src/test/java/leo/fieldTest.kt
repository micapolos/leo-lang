package leo

import leo.base.assertEqualTo
import leo.base.nullOf
import leo.base.string
import kotlin.test.Test

class FieldTest {
	@Test
	fun string() {
		numberWord.fieldTo(1.meta.term)
			.string
			.assertEqualTo("number #int 1")
	}

	@Test
	fun reflect() {
		oneWord.fieldTo(numberWord.term)
			.reflect
			.assertEqualTo(
				fieldWord fieldTo oneWord.fieldTo(numberWord.term).term)
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
			.assertEqualTo(letterWord fieldTo nullWord.term)
	}

	@Test
	fun orNullReflect_notNull() {
		Letter.A
			.orNullReflect(letterWord, Letter::reflect)
			.assertEqualTo(Letter.A.reflect)
	}
}
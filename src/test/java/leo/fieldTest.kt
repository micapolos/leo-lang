package leo

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
	fun reflect() {
		oneWord.fieldTo(Unit.metaTerm)
			.reflect { reflect }
			.assertEqualTo(
				fieldWord fieldTo term(
					oneWord fieldTo term(
						metaWord fieldTo Unit.reflect.term)))
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
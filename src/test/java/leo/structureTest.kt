package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class StructureTest {
	@Test
	fun string() {
		structure(
			oneWord fieldTo 1,
			twoWord fieldTo 2)
			.string
			.assertEqualTo("one ${1.meta.string}, two ${2.meta.string}")
	}

	@Test
	fun reflect() {
		structure<Nothing>(
			oneWord fieldTo numberWord,
			nameWord fieldTo stringWord)
			.reflect
			.assertEqualTo(structureWord fieldTo term(
				oneWord fieldTo numberWord,
				nameWord fieldTo stringWord))
	}

	@Test
	fun reflectAndParse() {
		structureWord
			.fieldTo<Nothing>(
				term(
					oneWord fieldTo numberWord,
					nameWord fieldTo stringWord))
			.parseStructure
			.assertEqualTo(
				structure(
					oneWord fieldTo numberWord,
					nameWord fieldTo stringWord))
	}
}
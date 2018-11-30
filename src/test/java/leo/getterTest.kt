package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class GetterTest {
	@Test
	fun firstWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.get(oneWord.getter)
			.assertEqualTo(numberWord.term)
	}

	@Test
	fun secondWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.get(nameWord.getter)
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun multipleWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term,
			oneWord fieldTo booleanWord.term)
			.get(oneWord.getter)
			.assertEqualTo(
				term(
					theWord fieldTo numberWord.term,
					theWord fieldTo booleanWord.term))
	}

	@Test
	fun missingWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.get(twoWord.getter)
			.assertEqualTo(null)
	}

	@Test
	fun structureLastWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.get(lastGetter)
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun structurePreviousWord() {
		term(
			oneWord fieldTo numberWord.term,
			nameWord fieldTo stringWord.term)
			.get(previousGetter)
			.assertEqualTo(term(oneWord fieldTo numberWord.term))
	}
}
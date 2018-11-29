package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class OneOfTest {
	@Test
	fun reflect() {
		oneOf(oneWord, twoWord)
			.reflect { term }
			.assertEqualTo(
				oneWord fieldTo term(
					ofWord fieldTo term(
						thisWord fieldTo oneWord.term,
						thisWord fieldTo twoWord.term)))
	}

	@Test
	fun reflectAndParse() {
		oneOf(oneWord, twoWord)
			.assertReflectAndParseWorks(
				{ reflect { term } },
				{ parseOneOf { wordTermOrNull?.word } })
	}
}
package leo.term

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		term(
			personWord apply term(
				firstWord apply term(nameWord apply stringWord.term),
				lastWord apply term(nameWord apply stringWord.term),
				ageWord apply numberWord.term))
			.string
			.assertEqualTo("person(first name string, last name string, age number)")
	}
}
package leo.term

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		script(
			personWord apply script(
				firstWord apply script(nameWord apply stringWord.script),
				lastWord apply script(nameWord apply stringWord.script),
				ageWord apply numberWord.script))
			.string
			.assertEqualTo("person(first name string, last name string, age number)")
	}
}
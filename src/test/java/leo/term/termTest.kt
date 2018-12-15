package leo.term

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import leo.base.the
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

	@Test
	fun match() {
		script(oneWord apply null)
			.matchArgument(oneWord) { the(this) }
			.assertEqualTo(the(null))

		script(oneWord apply numberWord.script)
			.matchArgument(oneWord) { the(this) }
			.assertEqualTo(the(numberWord.script))

		script(oneWord apply numberWord.script)
			.matchArgument(twoWord) { this }
			.assertEqualTo(null)

		script(
			oneWord apply numberWord.script,
			oneWord apply numberWord.script)
			.matchArgument(oneWord) { this }
			.assertEqualTo(null)
	}
}
package leo.term

import leo.base.assertEqualTo
import leo.numberWord
import leo.oneWord
import leo.theWord
import leo.uniqueWord
import kotlin.test.Test

class ValueTest {
	@Test
	fun scriptValue() {
		script(oneWord apply script(numberWord))
			.value
			.assertEqualTo(value(oneWord apply value(numberWord)))

		script(uniqueWord apply script(numberWord))
			.value
			.assertEqualTo(value(unique(numberWord.value)))

		script(theWord apply script(uniqueWord apply script(numberWord)))
			.value
			.assertEqualTo(value(theWord apply value(unique(numberWord.value))))
	}
}
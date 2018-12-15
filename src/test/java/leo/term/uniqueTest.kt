package leo.term

import leo.base.assertEqualTo
import leo.numberWord
import leo.uniqueWord
import leo.unitWord
import kotlin.test.Test

class UniqueTest {
	@Test
	fun parse() {
		script(uniqueWord apply unitWord.script)
			.parseUnique { this }
			.assertEqualTo(unitWord.script.unique)

		script(uniqueWord apply unitWord.script)
			.parseUnique { null }
			.assertEqualTo(null)

		script(numberWord apply unitWord.script)
			.parseUnique { this }
			.assertEqualTo(null)
	}
}
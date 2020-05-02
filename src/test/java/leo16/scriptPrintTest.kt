package leo16

import leo.base.assertEqualTo
import leo15.*
import kotlin.test.Test

class ScriptPrintTest {
	@Test
	fun list() {
		script(listName())
			.printScript
			.assertEqualTo(script(listName()))

		script(
			listName(bitName(zeroName())))
			.printScript
			.assertEqualTo(script(listName(bitName(zeroName()))))

		script(
			listName(
				bitName(zeroName()),
				bitName(oneName())))
			.printScript
			.assertEqualTo(script(listName(bitName(zeroName()), bitName(oneName()))))
	}
}
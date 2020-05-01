package leo16

import leo.base.assertEqualTo
import leo15.*
import kotlin.test.Test

class ScriptPrintTest {
	@Test
	fun list() {
		script(listName(emptyName()))
			.printScript
			.assertEqualTo(script(listName()))

		script(
			listName(linkName(
				previousName(listName(emptyName())),
				lastName(bitName(zeroName())))))
			.printScript
			.assertEqualTo(script(listName(bitName(zeroName()))))

		script(
			listName(linkName(
				previousName(listName(linkName(
					previousName(listName(emptyName())),
					lastName(bitName(zeroName()))))),
				lastName(bitName(oneName())))))
			.printScript
			.assertEqualTo(script(listName(bitName(zeroName()), bitName(oneName()))))
	}
}
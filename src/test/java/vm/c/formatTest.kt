package vm.c

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class FormatTest {
	@Test
	fun format() {
		"int x=1; int y=2;"
			.clangFormat
			.assertEqualTo(
				linesString(
					"int x = 1;",
					"int y = 2;"))
	}
}
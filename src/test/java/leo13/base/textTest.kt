package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.script
import leo13.scripter.scriptLine
import leo13.scripter.unsafeValue
import leo9.stack
import kotlin.test.Test

class TextTest {
	@Test
	fun scriptLine() {
		textScripter
			.scriptLine(text(" \t"))
			.assertEqualTo(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackType(byteScripter)
							.script(
								stack(
									byte(32),
									byte(9))))))
	}

	@Test
	fun unsafeValue() {
		textScripter
			.unsafeValue(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackType(byteScripter)
							.script(
								stack(
									byte(32),
									byte(9))))))
			.assertEqualTo(text(" \t"))
	}
}
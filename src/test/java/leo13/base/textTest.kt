package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import leo13.stack
import kotlin.test.Test

class TextTest {
	@Test
	fun scriptLine() {
		textWriter
			.scriptLine(text(" \t"))
			.assertEqualTo(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackWriter(byteWriter)
							.script(
								stack(
									byte(32),
									byte(9))))))
	}

	@Test
	fun unsafeValue() {
		textReader
			.unsafeValue(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackWriter(byteWriter)
							.script(
								stack(
									byte(32),
									byte(9))))))
			.assertEqualTo(text(" \t"))
	}
}
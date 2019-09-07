package leo13.base

import leo.base.assertEqualTo
import leo13.base.type.script
import leo13.base.type.scriptLine
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import leo9.stack
import kotlin.test.Test

class TextTest {
	@Test
	fun scriptLine() {
		textType
			.scriptLine(text(" \t"))
			.assertEqualTo(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackType(byteType)
							.script(
								stack(
									byte(32),
									byte(9))))))
	}

	@Test
	fun unsafeValue() {
		textType
			.unsafeValue(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo stackType(byteType)
							.script(
								stack(
									byte(32),
									byte(9))))))
			.assertEqualTo(text(" \t"))
	}
}
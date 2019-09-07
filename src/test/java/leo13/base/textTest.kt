package leo13.base

import leo.base.assertEqualTo
import leo.base.byte
import leo13.base.type.scriptLine
import leo13.base.type.unsafeValue
import leo13.base.typed.byteType
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class TextTest {
	@Test
	fun type() {
		text(" \t")
			.typedScriptLine
			.assertEqualTo(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo script(
							"byte" lineTo script(
								"list" lineTo script(
									byteType.scriptLine(byte(32)),
									byteType.scriptLine(byte(9))))))))

		textType
			.unsafeValue(
				"text" lineTo script(
					"utf" lineTo script(
						"eight" lineTo script(
							"byte" lineTo script(
								"list" lineTo script(
									byteType.scriptLine(byte(32)),
									byteType.scriptLine(byte(9))))))))
			.assertEqualTo(text(" \t"))
	}
}
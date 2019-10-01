package leo13.decompiler

import leo.base.assertEqualTo
import leo.base.byte
import leo.binary.bit0
import leo.binary.bit1
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.type.*
import kotlin.test.Test

class DecompilerTest {
	@Test
	fun typeScript() {
		type()
			.script(null)
			.assertEqualTo(script())

		type(
			"x" lineTo type("one"),
			"y" lineTo type("two"))
			.script(null)
			.assertEqualTo(
				script(
					"x" lineTo script("one"),
					"y" lineTo script("two")))

		type(options("zero", "one"))
			.script(null to null)
			.assertEqualTo(script("one"))

		type(options("zero", "one"))
			.script((null to null) to null)
			.assertEqualTo(script("zero"))

		type("bitek" lineTo type(options("zero", "one")))
			.script(null to null)
			.assertEqualTo(script("bitek" lineTo script("one")))

		type("bitek" lineTo type(options("zero", "one")))
			.script((null to null) to null)
			.assertEqualTo(script("bitek" lineTo script("zero")))
	}

	@Test
	fun core() {
		booleanTypeLine.scriptLine(false).assertEqualTo(false.scriptLine)
		booleanTypeLine.scriptLine(true).assertEqualTo(true.scriptLine)

		bitTypeLine.scriptLine(bit0).assertEqualTo(bit0.scriptLine)
		bitTypeLine.scriptLine(bit1).assertEqualTo(bit1.scriptLine)

		byteTypeLine.scriptLine(byte(0)).assertEqualTo(byte(0).scriptLine)
		byteTypeLine.scriptLine(byte(255)).assertEqualTo(byte(255).scriptLine)
	}
}
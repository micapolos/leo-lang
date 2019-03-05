package leo.script

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo.base.string
import leo.binary.bit
import org.junit.Test

class ScriptLinesTest {
	@Test
	fun boolean() {
		false.scriptLine.string.assertEqualTo("boolean false   ")
	}

	@Test
	fun bit() {
		0.bit.scriptLine.string.assertEqualTo("bit zero   ")
	}

	@Test
	fun byte() {
		13.clampedByte.scriptLine.string
			.assertEqualTo("byte bit zero   bit zero   bit zero   bit zero   bit one   bit one   bit zero   bit one    ")
	}

	@Test
	fun string() {
		"\u000d".scriptLine.string
			.assertEqualTo("string utf eight byte bit zero   bit zero   bit zero   bit zero   bit one   bit one   bit zero   bit one       ")
	}
}

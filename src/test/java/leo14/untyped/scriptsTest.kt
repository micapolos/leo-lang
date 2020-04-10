package leo14.untyped

import leo.base.clampedByte
import leo.base.println
import leo.binary.bit
import leo.binary.zero
import leo14.untyped.typed.script
import kotlin.test.Test

class ScriptsTest {
	@Test
	fun test() {
		zero.bit.script.println
		14.clampedByte.script.println
		123.script.println
		byteArrayOf(0, 1, 10).script.println
		"Hello, world!".script.println
	}
}
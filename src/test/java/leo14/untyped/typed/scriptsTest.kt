package leo14.untyped.typed

import leo.base.clampedByte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo14.untyped.leoString
import kotlin.test.Test

class ScriptsTest {
	@Test
	fun test() {
		zeroBit.script.leoString//.println
		oneBit.script.leoString//.println
		false.script.leoString//.println
		true.script.leoString//.println
		14.clampedByte.script.leoString//.println
		123.script.leoString//.println
		byteArrayOf(0, 1, 10).script.leoString//.println
		"Hello, world!".script.leoString//.println
	}
}
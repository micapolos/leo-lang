package leo16

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo13.base.oneBit
import leo13.base.zeroBit
import kotlin.test.Test

class ParseScriptTest {
	@Test
	fun bit() {
		zeroBit.run { script(expandSentence).bitOrNull.assertEqualTo(this) }
		oneBit.run { script(expandSentence).bitOrNull.assertEqualTo(this) }
	}

	@Test
	fun byte() {
		13.clampedByte.run { script(expandSentence).byteOrNull.assertEqualTo(this) }
	}

	@Test
	fun int() {
		65539.run { script(expandSentence).intOrNull.assertEqualTo(this) }
	}
}
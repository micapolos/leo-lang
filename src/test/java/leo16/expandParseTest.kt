package leo16

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.stack
import kotlin.test.Test

class ExpandParseTest {
	@Test
	fun bit() {
		zeroBit.run { expandSentence.bitOrNull.assertEqualTo(this) }
		oneBit.run { expandSentence.bitOrNull.assertEqualTo(this) }
	}

	@Test
	fun byte() {
		13.clampedByte.run { expandSentence.byteOrNull.assertEqualTo(this) }
	}

	@Test
	fun int() {
		65539.run { expandSentence.intOrNull.assertEqualTo(this) }
	}

	@Test
	fun sentenceStack() {
		stack<Nothing>()
			.run {
				expandSentence
					.parseSentenceStackOrNull
					.assertEqualTo(this)
			}

		stack("bit"("zero"()), "bit"("one"()))
			.run {
				expandSentence
					.parseSentenceStackOrNull
					.assertEqualTo(this)
			}
	}

	@Test
	fun string() {
		"Hello".run { expandSentence.stringOrNull.assertEqualTo(this) }
	}
}
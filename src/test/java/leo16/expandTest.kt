package leo16

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.stack
import leo15.*
import kotlin.test.Test

class ExpandTest {
	@Test
	fun bit() {
		zeroBit.expandSentence.assertEqualTo(bitName(zeroName()))
		oneBit.expandSentence.assertEqualTo(bitName(oneName()))
	}

	@Test
	fun byte() {
		13.clampedByte.expandSentence
			.assertEqualTo(
				byteName(bitName(listName(
					bitName(zeroName()),
					bitName(zeroName()),
					bitName(zeroName()),
					bitName(zeroName()),
					bitName(oneName()),
					bitName(oneName()),
					bitName(zeroName()),
					bitName(oneName())))))
	}

	@Test
	fun stack() {
		stack<Sentence>().expandSentence.assertEqualTo(listName())

		stack("bit"("zero"()), "bit"("one"()))
			.expandSentence
			.assertEqualTo(listName(bitName(zeroName()), bitName(oneName())))
	}
}
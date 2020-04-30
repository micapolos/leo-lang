package leo16

import leo.base.assertEqualTo
import leo.base.clampedByte
import leo15.*
import kotlin.test.Test

class ExpandScriptTest {
	@Test
	fun byte() {
		13.clampedByte
			.expandSentence
			.assertEqualTo(
				byteName(
					firstName(bitName(zeroName())),
					secondName(bitName(zeroName())),
					thirdName(bitName(zeroName())),
					fourthName(bitName(zeroName())),
					fifthName(bitName(oneName())),
					sixthName(bitName(oneName())),
					seventhName(bitName(zeroName())),
					eighthName(bitName(oneName()))))
	}
}
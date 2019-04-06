package leo.arch

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class WordTest {
	@Test
	fun incDec() {
		archTest { testIncDec }
	}
}

val Arch.testIncDec get() =
	this
		.run { zero.assertFails { dec } }
		.run { zero.inc.dec.assertEqualTo(zero) }
		.run { zero.inc.inc.dec.dec.assertEqualTo(zero) }
		.run { max.assertFails { inc } }
		.run { max.dec.inc.assertEqualTo(max) }
		.run { max.dec.dec.inc.inc.assertEqualTo(max) }


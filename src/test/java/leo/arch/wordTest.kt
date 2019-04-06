package leo.arch

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class WordTest {
	@Test
	fun all() {
		archTest {
			this
				.run { zero.assertFails { dec } }
				.run { zero.inc.dec.assertEqualTo(zero) }
				.run { zero.inc.inc.dec.dec.assertEqualTo(zero) }

				.run { max.assertFails { inc } }
				.run { max.dec.inc.assertEqualTo(max) }
				.run { max.dec.dec.inc.inc.assertEqualTo(max) }

				.run { zero.plus(zero).assertEqualTo(zero) }
				.run { zero.plus(zero.inc).assertEqualTo(zero.inc) }
				.run { zero.plus(zero.inc.inc).assertEqualTo(zero.inc.inc) }
				.run { max.plus(zero).assertEqualTo(max) }
				// TODO .run { max.assertFails { plus(zero.inc) } }

				.run { max.minus(zero).assertEqualTo(max) }
				.run { max.minus(zero.inc).assertEqualTo(max.dec) }
				.run { max.minus(zero.inc.inc).assertEqualTo(max.dec.dec) }
				.run { zero.minus(zero).assertEqualTo(zero) }
			  // TODO .run { zero.assertFails { minus(zero.inc) } }
		}
	}
}

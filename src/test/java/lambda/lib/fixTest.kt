package lambda.lib

import lambda.invoke
import lambda.term
import leo.base.assertEqualTo
import kotlin.test.Test

class FixTest {
	@Test
	fun math() {
		val rebuildNat = fix(
			term { fn ->
				term { nat ->
					nat.natForVoidOrDec(
						const(zeroNat),
						term { nat -> fn(nat.natInc) })
				}
			})

		rebuildNat(zeroNat).assertEqualTo(zeroNat)
	}
}
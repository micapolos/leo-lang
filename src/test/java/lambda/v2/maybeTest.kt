package lambda.v2

import leo.base.assertEqualTo
import kotlin.test.Test

class MaybeTest {
	@Test
	fun test() {
		nothing
			.forNothingOrJust(zero.constant, zero.succ)
			.eval
			.assertEqualTo(zero)

		just(zero.succ)
			.forNothingOrJust(zero.constant, succ)
			.eval
			.assertEqualTo(zero.succ.succ)
	}
}
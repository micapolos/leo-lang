package leo7.frp

import kotlin.test.Test

class FrpTest {
	@Test
	fun scene() {
		animation(
			start(number(12)),
			speed(number(1) per second))
	}
}

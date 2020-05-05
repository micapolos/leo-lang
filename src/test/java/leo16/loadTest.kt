package leo16

import leo.base.assertEqualTo
import leo15.*
import kotlin.test.Test

class LoadTest {
	@Test
	fun loadClassName() {
		value(testingName(pingName()))
			.loadOrNull
			.assertEqualTo(value(pongName()))
	}
}
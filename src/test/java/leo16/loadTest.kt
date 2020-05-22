package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import leo16.names.*
import kotlin.test.Test

class LoadTest {
	@Test
	fun _loadClass() {
		value(_testing(_ping()))
			.loadOrNull
			.assertEqualTo(compile_ { ping.is_ { pong } })
	}
}
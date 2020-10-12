package leo19

import leo.base.assertEqualTo
import leo14.invoke
import leo14.script
import leo16.names.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun endToEnd() {
		script(
			_bit(_zero()),
			_as(_bit(_choice(_zero(), _one()))))
			.eval
			.assertEqualTo(null)
	}
}
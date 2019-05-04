package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

class ScopeTest {
	@Test
	fun bypass() {
		val variable = variable()
		scopeString {
			val scope = scope(executor(this, treo(capture(variable), treo(put(value(variable)), treo(back.back)))))
			scope.putBit("0010")
		}.assertEqualTo("0010")
	}
}


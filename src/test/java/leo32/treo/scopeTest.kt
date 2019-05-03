package leo32.treo

import kotlin.test.Test

class ScopeTest {
	@Test
	fun bypass() {
		val variable = variable()
		val scope = scope(executor(scope(printDigitSink), treo(value(variable), treo(put(value(variable)), treo(back.back)))))
		scope.putBit("0010")
	}
}
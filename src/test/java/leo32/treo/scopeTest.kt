package leo32.treo

import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class ScopeTest {
	@Test
	fun bypass() {
		val variable = variable()
		val scope = scope(executor(treo(value(variable), treo(put(value(variable)), treo(back.back)))))

		scope.run {
			put(bit0)
			put(bit1)
		}
	}
}
package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class FieldReaderTest {
	@Test
	fun plus() {
		empty
			.fieldReader
			.plus(
				defineSymbol to term(
					zeroSymbol to term(),
					givesSymbol to term(oneSymbol)))
			.plus(zeroSymbol to term())
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}
}
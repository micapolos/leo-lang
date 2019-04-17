package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ByteReaderTest {
	@Test
	fun string() {
		empty
			.byteReader
			.plus("define zero  gives one    zero  ")!!
			.symbolReader
			.fieldReader
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}
}
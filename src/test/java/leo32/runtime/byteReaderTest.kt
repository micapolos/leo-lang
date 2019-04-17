package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ByteReaderTest {
	@Test
	fun define() {
		empty
			.byteReader
			.plus("define zero  gives one    zero  ")!!
			.symbolReader
			.fieldReader
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun quoting() {
		empty
			.byteReader
			.plus("quote define zero  gives one    ")!!
			.symbolReader
			.fieldReader
			.term
			.script
			.assertEqualTo(
				script(
					defineSymbol to script(
						zeroSymbol to script(),
						givesSymbol to script(oneSymbol))))
	}
}
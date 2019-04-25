package leo32

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ByteReaderTest {
	@Test
	fun define() {
		empty
			.byteReader
			.plus("zero..gives.one...zero..".core)!!
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
			.plus("quote.zero..gives.one...".core)!!
			.symbolReader
			.fieldReader
			.term
			.script
			.assertEqualTo(
				script(
					zeroSymbol to script(),
					givesSymbol to script(oneSymbol)))
	}
}
package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class SymbolReaderTest {
	@Test
	fun plus() {
		empty
			.symbolReader
			.plus(defineSymbol)!!
			.plus(zeroSymbol)!!
			.plus(null)!!
			.plus(givesSymbol)!!
			.plus(oneSymbol)!!
			.plus(null)!!
			.plus(null)!!
			.plus(null)!!
			.plus(zeroSymbol)!!
			.plus(null)!!
			.fieldReader
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun quote() {
		empty
			.symbolReader
			.plus(quoteSymbol)!!
			.plus(defineSymbol)!!
			.plus(zeroSymbol)!!
			.plus(null)!!
			.plus(givesSymbol)!!
			.plus(oneSymbol)!!
			.plus(null)!!
			.plus(null)!!
			.plus(null)!!
			.fieldReader
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}
}
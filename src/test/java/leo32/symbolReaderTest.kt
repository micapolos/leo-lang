package leo32

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test


class SymbolReaderTest {
	@Test
	fun plus() {
		empty
			.symbolReader
			.plus(zeroSymbol)!!
			.plus(null)!!
			.plus(givesSymbol)!!
			.plus(oneSymbol)!!
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
			.plus(zeroSymbol)!!
			.plus(null)!!
			.plus(givesSymbol)!!
			.plus(oneSymbol)!!
			.plus(null)!!
			.plus(null)!!
			.fieldReader
			.term
			.script
			.assertEqualTo(
				script(
					zeroSymbol to script(),
					givesSymbol to script(oneSymbol)))
	}

	@Test
	fun doubleQuote() {
		empty
			.symbolReader
			.plus(quoteSymbol)!!
			.plus(quoteSymbol)!!
			.plus(zeroSymbol)!!
			.plus(null)!!
			.plus(null)!!
			.plus(null)!!
			.fieldReader
			.term
			.script
			.assertEqualTo(script(quoteSymbol to script(zeroSymbol)))
	}
}
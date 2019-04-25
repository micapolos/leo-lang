package leo32

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class FieldReaderTest {
	@Test
	fun plusSimpleTerm() {
		empty
			.fieldReader
			.plus(zeroSymbol to term())
			.term
			.script
			.assertEqualTo(script(zeroSymbol))
	}

	@Test
	fun plus() {
		empty
			.fieldReader
			.plus(zeroSymbol to term())
			.plus(givesSymbol to term(oneSymbol))
			.plus(zeroSymbol to term())
			.term
			.script
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun wrapping() {
		empty
			.fieldReader
			.plus(zeroSymbol to term())
			.plus(oneSymbol to term())
			.term
			.script
			.assertEqualTo(script(oneSymbol to script(zeroSymbol)))
	}
}
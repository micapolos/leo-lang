package leo32.runtime

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
			.plus(
				defineSymbol to term(
					zeroSymbol to term(),
					givesSymbol to term(oneSymbol)))
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
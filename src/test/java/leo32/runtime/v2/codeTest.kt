package leo32.runtime.v2

import leo.base.assertEqualTo
import leo.base.string
import leo32.runtime.*
import kotlin.test.Test

class CodeTest {
	@Test
	fun code() {
		script(zeroSymbol)
			.code
			.string
			.assertEqualTo("zero")

		script(zeroSymbol)
			.plus(oneSymbol)
			.code
			.string
			.assertEqualTo("one zero")

		script(zeroSymbol)
			.plus(oneSymbol to script(twoSymbol))
			.code
			.string
			.assertEqualTo("zero\none two")

		script(zeroSymbol)
			.plus(oneSymbol to script(twoSymbol))
			.plus(twoSymbol to script(oneSymbol))
			.code
			.string
			.assertEqualTo("zero\none two\ntwo one")

		script(zeroSymbol to script(oneSymbol to script(twoSymbol)))
			.code
			.string
			.assertEqualTo("zero one two")

		script(zeroSymbol to script(
			xSymbol to script(zeroSymbol),
			ySymbol to script(oneSymbol)))
			.code
			.string
			.assertEqualTo("zero\n\tx zero\n\ty one")

		script(
			circleSymbol to script(
				radiusSymbol to script(zeroSymbol),
				centerSymbol to script(
					xSymbol to script(zeroSymbol),
					ySymbol to script(oneSymbol))))
			.code
			.string
			.assertEqualTo("circle\n\tradius zero\n\tcenter\n\t\tx zero\n\t\ty one")
	}
}

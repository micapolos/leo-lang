package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class LeoReaderTest {
	@Test
	fun symbol() {
		empty
			.leoReader
			.plus("zero")!!
			.termOrNull!!
			.script
			.assertEqualTo(script(zeroSymbol))
	}

	@Test
	fun field() {
		empty
			.leoReader
			.plus("zero one")!!
			.termOrNull!!
			.script
			.assertEqualTo(script(zeroSymbol to script(oneSymbol)))
	}

	@Test
	fun fields() {
		empty
			.leoReader
			.plus("x one\ny two")!!
			.termOrNull!!
			.script
			.assertEqualTo(
				script(
					xSymbol to script(oneSymbol),
					ySymbol to script(twoSymbol)))
	}

	@Test
	fun complex() {
		empty
			.leoReader
			.plus("circle\n\tradius zero\n\tcenter\n\t\tx one\n\t\ty two")!!
			.termOrNull!!
			.script
			.assertEqualTo(
				script(
					circleSymbol to script(
						radiusSymbol to script(zeroSymbol),
						centerSymbol to script(
							xSymbol to script(oneSymbol),
							ySymbol to script(twoSymbol)))))
	}

	@Test
	fun newlines() {
		empty
			.leoReader
			.plus("center\n\tx zero\n\n\ty one")!!
			.termOrNull!!
			.script
			.assertEqualTo(
				script(
					centerSymbol to script(
						xSymbol to script(zeroSymbol),
						ySymbol to script(oneSymbol))))
	}

	@Test
	fun errors() {
		empty
			.leoReader
			.apply { plus(" ").assertEqualTo(null) }
			.apply { plus("\t").assertEqualTo(null) }
			.apply { plus("a\t").assertEqualTo(null) }
			.apply { plus("a  ").assertEqualTo(null) }
			.apply { plus("a \t").assertEqualTo(null) }
			.apply { plus("a \t").assertEqualTo(null) }
	}
}
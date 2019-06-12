package leo5.lang

import leo.base.assertEqualTo
import leo5.script.lineTo
import leo5.script.script
import leo5.script.writeScript
import kotlin.test.Test

class LangTest {
	@Test
	fun lang() {
		writeScript {
			bit(zero)
			plus(bit(zero), negate)
			plus { bit(zero).negate }
			plus { bit(zero); negate }
			plus {
				bit(zero)
				negate
			} + int(12) + float(13) + zero
		}.assertEqualTo(
			script(
				"bit" lineTo script("zero"),
				"plus" lineTo script(
					"bit" lineTo script("zero"),
					"negate" lineTo script()),
				"plus" lineTo script(
					"bit" lineTo script("zero"),
					"negate" lineTo script()),
				"plus" lineTo script(
					"bit" lineTo script("zero"),
					"negate" lineTo script()),
				"plus" lineTo script(
					"bit" lineTo script("zero"),
					"negate" lineTo script()),
				"plus" lineTo script(
					"int" lineTo script("12")),
				"plus" lineTo script(
					"float" lineTo script("13")),
				"plus" lineTo script("zero")))
	}

	@Test
	fun writeCircle() {
		writeScript {
			circle {
				radius = float(12)
				center {
					x = float(13)
					y = float(15)
				}
			}
		}.assertEqualTo(
			script(
				"circle" lineTo script(
					"radius" lineTo script(
						"float" lineTo script("12")),
					"center" lineTo script(
						"x" lineTo script(
							"float" lineTo script("13")),
						"y" lineTo script(
							"float" lineTo script("15"))))))
	}
}

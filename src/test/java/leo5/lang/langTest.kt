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
			plus(bit(zero), negate())
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
				radius = float(12) + float(13).negate
				center {
					x = float(13)
					y = float(15)
				}
			}
		}.assertEqualTo(
			script(
				"circle" lineTo script(
					"radius" lineTo script(
						"float" lineTo script("12"),
						"plus" lineTo script(
							"float" lineTo script("13"),
							"negate" lineTo script())),
					"center" lineTo script(
						"x" lineTo script(
							"float" lineTo script("13")),
						"y" lineTo script(
							"float" lineTo script("15"))))))
	}

	@Test
	fun simplePlusSimple() {
		writeScript { zero + one }
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script("one")))
	}

	@Test
	fun complexPlusSimple() {
		writeScript { zero.negate + one }
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"negate" lineTo script(),
					"plus" lineTo script("one")))
	}

	@Test
	fun simplePlusComplex() {
		writeScript { zero + one.negate }
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script(
						"one" lineTo script(),
						"negate" lineTo script())))
	}

	@Test
	fun complexPlusComplex() {
		writeScript { zero.negate + one.negate }
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"negate" lineTo script(),
					"plus" lineTo script(
						"one" lineTo script(),
						"negate" lineTo script())))
	}

	@Test
	fun simpleAssignment() {
		writeScript { x = zero }
			.assertEqualTo(
				script(
					"x" lineTo script("zero")))
	}

	@Test
	fun complexAssignment() {
		writeScript { x = zero.negate }
			.assertEqualTo(
				script(
					"x" lineTo script(
						"zero" lineTo script(),
						"negate" lineTo script())))
	}

	@Test
	fun simpleAssignments() {
		writeScript {
			x = zero
			y = one
		}.assertEqualTo(
			script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
	}

	@Test
	fun complexAssignments() {
		writeScript {
			x = zero.negate
			y = one.negate
		}.assertEqualTo(
			script(
				"x" lineTo script(
					"zero" lineTo script(),
					"negate" lineTo script()),
				"y" lineTo script(
					"one" lineTo script(),
					"negate" lineTo script())))
	}
}

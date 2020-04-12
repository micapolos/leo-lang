package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.Script
import leo14.untyped.dsl2.*
import kotlin.test.Test

fun Script.testGives_(f: F) =
	eval.assertEqualTo(script_(f))

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.testGives_ { number(10) }
		script_ { text("foo") }.testGives_ { text("foo") }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.testGives_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}
	}

	@Test
	fun fieldAccess() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.x
		}.testGives_ {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.testGives_ {
			y { number(20) }
		}
	}

	@Test
	fun normalization() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.center
		}.testGives_ {
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
	}
}
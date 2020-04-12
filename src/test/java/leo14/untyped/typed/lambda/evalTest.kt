package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.Script
import leo14.untyped.dsl2.*
import kotlin.test.Test

fun Script.assertEvals(f: F) =
	eval.assertEqualTo(script_(f))

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.assertEvals { number(10) }
		script_ { text("foo") }.assertEvals { text("foo") }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.assertEvals {
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
		}.assertEvals {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.assertEvals {
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
		}.assertEvals {
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
	}
}
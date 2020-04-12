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
	}

	@Test
	fun script() {
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
}
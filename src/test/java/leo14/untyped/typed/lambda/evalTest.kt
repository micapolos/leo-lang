package leo14.untyped.typed.lambda

import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.gives_ { number(10) }
		script_ { text("foo") }.gives_ { text("foo") }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.gives_ {
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
		}.gives_ {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.gives_ {
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
		}.gives_ {
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
	}
}
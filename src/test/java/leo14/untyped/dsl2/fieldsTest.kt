package leo14.untyped.dsl2

import kotlin.test.Test

class FieldsTest {
	@Test
	fun _test() {
		_run {
			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.x
				gives { x { number(10) } }
			}

			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.y
				gives { y { number(20) } }
			}

			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.z
				gives {
					point {
						x { number(10) }
						y { number(20) }
					}.z
				}
			}

			assert {
				my
				point {
					x { number(10) }
					y { number(20) }
				}.x
				gives {
					my
					point {
						x { number(10) }
						y { number(20) }
					}.x
				}
			}
		}
	}
}
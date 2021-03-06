package leo14.untyped.dsl2

import kotlin.test.Test

class FieldsTest {
	@Test
	fun test_() {
		run_ {
			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.x
				equals_ { x { number(10) } }
			}

			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.y
				equals_ { y { number(20) } }
			}

			assert {
				point {
					x { number(10) }
					y { number(20) }
				}.z
				equals_ {
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
				equals_ {
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
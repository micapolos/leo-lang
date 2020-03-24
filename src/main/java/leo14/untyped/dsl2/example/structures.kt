package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	run_ {
		circle
		gives {
			circle {
				radius { number }
			}
		}

		square
		gives {
			square {
				side { number }
			}
		}

		triangle
		gives {
			triangle {
				base { number }
				height { number }
			}
		}

		shape
		gives {
			shape {
				either {
					it { circle }
					it { square }
					it { triangle }
				}
			}
		}

		circle.area
		does {
			area {
				number(3.14159)
				times { given.circle.radius.number }
				times { given.circle.radius.number }
			}
		}

		square.area
		does {
			area {
				given.square.side.number
				times { given.square.side.number }
			}
		}

		triangle.area
		does {
			area {
				given.triangle.base.number
				times { given.triangle.height.number }
				times { number(0.5) }
			}
		}

		shape.area
		does {
			given.shape
			match {
				circle { matching.circle.area }
				square { matching.square.area }
				triangle { matching.triangle.area }
			}
		}

		assert {
			shape { circle { radius { number(10) } } }.area
			gives { area { number(3.14159).times { number(100) } } }
		}

		assert {
			shape { square { side { number(2) } } }.area
			gives { area { number(4) } }
		}

		assert {
			shape {
				triangle {
					base { number(4) }
					height { number(10) }
				}
			}.area
			gives { area { number(20.0) } }
		}
	}
}
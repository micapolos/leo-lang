package leo20.dsl

import leo15.dsl.*

val make_ = dsl_ {
	test {
		x { zero }
		y { one }
		make { point }
		equals_ {
			point {
				x { zero }
				y { one }
			}
		}
	}

	test {
		x { zero }
		y { one }
		make { center { point } }
		equals_ {
			center {
				point {
					x { zero }
					y { one }
				}
			}
		}
	}
}
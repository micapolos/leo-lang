package leo20.dsl

import leo15.dsl.*

val get_ = dsl_ {
	test {
		point {
			x { number(10) }
			y { number(20) }
		}
		get { x }
		equals_ { x { number(10) } }
	}

	test {
		point {
			x { number(10) }
			y { number(20) }
		}
		get { y }
		equals_ { y { number(20) } }
	}

	test {
		point {
			x { number(10) }
			y { number(20) }
		}
		get { x { number } }
		equals_ { number(10) }
	}

	test {
		x { number(10) }
		y { get { x { number } } }
		equals_ {
			x { number(10) }
			y { number(10) }
		}
	}
}
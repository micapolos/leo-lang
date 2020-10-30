package leo20.dsl

import leo15.dsl.*

val save_ = dsl_ {
	test {
		save { x { number(10) } }
		get { x { number } }
		equals_ { number(10) }
	}

	test {
		save { x { number(10) } }
		save {
			x {
				get { x { number } }
				do_ { plus { number(1) } }
			}
		}
		get { x { number } }
		equals_ { number(11) }
	}

	test {
		save { x { number(10) } }
		save {
			y {
				get { x { number } }
				do_ { plus { number(1) } }
			}
		}
		get { y { number } }
		equals_ { number(11) }
	}
}
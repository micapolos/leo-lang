package leo20.dsl

import leo15.dsl.*

val scope_ = dsl_ {
	test {
		get { x }
		fails
	}

	test {
		x { zero }
		y { get { x } }
		equals_ {
			quote {
				x { zero }
				y { x { zero } }
			}
		}
	}

	test {
		x { zero }
		y { get { y } }
		fails
	}
}
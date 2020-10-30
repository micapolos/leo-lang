package leo20.dsl

import leo15.dsl.*

val give_ = dsl_ {
	test {
		x { zero }
		y { one }
		give {
			get { x }
			plus { get { y } }
		}
		equals_ {
			x { zero }
			plus { y { one } }
		}
	}
}
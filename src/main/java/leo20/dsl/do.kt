package leo20.dsl

import leo15.dsl.*

val do__ = dsl_ {
	test {
		x { zero }
		y { one }
		do_ {
			get { x }
			plus { get { y } }
		}
		equals_ {
			x { zero }
			plus { y { one } }
		}
	}
}
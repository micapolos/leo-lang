package leo20.dsl

import leo15.dsl.*

val function_ = dsl_ {
	test {
		function { get { x } }
		apply {
			x { number(10) }
			y { number(20) }
		}
		equals_ { x { number(10) } }
	}
}
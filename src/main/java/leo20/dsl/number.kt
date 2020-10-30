package leo20.dsl

import leo15.dsl.*

val number_ = dsl_ {
	test {
		number(10)
		do_ { plus { number(20) } }
		equals_ { number(30) }
	}

	test {
		number(30)
		do_ { minus { number(20) } }
		equals_ { number(10) }
	}

	test {
		number(10)
		do_ { equals_ { number(10) } }
		equals_ { boolean { true_ } }
	}

	test {
		number(10)
		do_ { equals_ { number(20) } }
		equals_ { boolean { false_ } }
	}
}
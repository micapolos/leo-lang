package leo20.dsl

import leo15.dsl.*

val number_ = dsl_ {
	test {
		number(10)
		plus { number(20) }
		equals_ { number(30) }
	}

	test {
		number(30)
		minus { number(20) }
		equals_ { number(10) }
	}

	test {
		number(10)
		equals_ { number(10) }
		equals_ { boolean { true_ } }
	}

	test {
		number(10)
		equals_ { number(20) }
		equals_ { boolean { false_ } }
	}
}
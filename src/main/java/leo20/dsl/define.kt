package leo20.dsl

import leo15.dsl.*

val define_ = dsl_ {
	test {
		bit { zero }
		define {
			zero
			does { one }
		}
		equals_ { bit { zero } }
	}

	test {
		define {
			zero
			does { one }
		}
		zero
		equals_ { one }
	}

	test {
		define {
			number { any }
			increment
			does {
				get { number }
				plus { number(1) }
			}
		}
		number(10)
		increment
		equals_ { number(11) }
	}
}

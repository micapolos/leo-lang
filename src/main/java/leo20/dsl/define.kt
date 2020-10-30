package leo20.dsl

import leo15.dsl.*

val define_ = dsl_ {
	test {
		define {
			zero
			does { one }
		}
		equals_ { nothing_ }
	}

	test {
		define {
			zero
			does { one }
		}
		do_ { zero }
		equals_ { one }
	}

	test {
		define {
			number { any }
			increment
			does {
				get { number }
				do_ { plus { number(1) } }
			}
		}
		number(10)
		do_ { increment }
		equals_ { number(11) }
	}
}

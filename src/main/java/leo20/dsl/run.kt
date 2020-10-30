package leo20.dsl

import leo15.dsl.*

val run_ = dsl_ {
	test {
		define {
			zero
			does { one }
		}
		run { zero }
		equals_ { one }
	}

	test {
		define {
			zero
			does { one }
		}
		run { one }
		fails
	}
}
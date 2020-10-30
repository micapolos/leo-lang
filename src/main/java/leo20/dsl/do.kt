package leo20.dsl

import leo15.dsl.*

val run_ = dsl_ {
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
			zero
			does { one }
		}
		do_ { one }
		fails
	}
}
package leo20.dsl

import leo15.dsl.*

val resolve_ = dsl_ {
	test {
		define {
			zero
			does { one }
		}
		resolve { zero }
		equals_ { one }
	}

	test {
		define {
			zero
			does { one }
		}
		resolve { one }
		fails
	}
}
package leo20.dsl

import leo15.dsl.*

val quote_ = dsl_ {
	test {
		define {
			x { any }
			does { zero }
		}
		define {
			y { any }
			does { one }
		}
		quote { x { y { z } } }
		equals_ { x { y { z } } }
	}
}
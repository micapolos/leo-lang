package leo20.dsl

import leo15.dsl.*

val with_ = dsl_ {
	test {
		define {
			x { any }
			does { zero }
		}
		define {
			y { any }
			does { one }
		}
		with { x { y { z } } }
		equals_ { x { one } }
	}
}
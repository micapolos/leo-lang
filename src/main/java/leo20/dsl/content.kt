package leo20.dsl

import leo15.dsl.*

val content_ = dsl_ {
	test {
		define {
			x { any }
			does { zero }
		}
		define {
			y { any }
			does { one }
		}
		content { x { do_ { y { z } } } }
		equals_ { x { one } }
	}
}
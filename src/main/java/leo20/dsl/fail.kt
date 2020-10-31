package leo20.dsl

import leo15.dsl.*

val fail_ = dsl_ {
	test {
		number(10)
		fail
		fails
	}
}
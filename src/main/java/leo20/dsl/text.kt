package leo20.dsl

import leo15.dsl.*

val text_ = dsl_ {
	test {
		text("Hello, ")
		do_ { append { text("world!") } }
		equals_ { text("Hello, world!") }
	}
}
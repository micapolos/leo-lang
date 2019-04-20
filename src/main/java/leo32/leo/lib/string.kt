package leo32.leo.lib

import leo32.leo.*

val stringLib: Leo = {
	test {
		string("Hello world!")
		gives { string { plus("Hello world!") } }
	}

	test {
		string("Hello ")
		plus { string("world!") }
		gives { string("Hello world!") }
	}
}

fun main() {
	_test(stringLib)
}

package leo.base

import kotlin.test.Test

fun fibf(i: Int): Int =
	when (i) {
		1 -> 1
		2 -> 1
		else -> fibf(i.dec()) + fibf(i.dec().dec())
	}

val fib = fix { i: Int ->
	when (i) {
		1 -> 1
		2 -> 1
		else -> this(i.dec()) + this(i.dec().dec())
	}
}

class FixTest {
	@Test
	fun fibo() {
		fib(10).assertEqualTo(55)
	}

	@Test
	fun performance() {
		repeat(10) {
			println("======")
			printTime("fun: ") { fibf(36) }
			printTime("fix: ") { fib(36) }
		}
	}
}

package leo.base

import kotlin.test.Test

fun funFib(i: Int): Int =
	when (i) {
		0 -> 0
		1 -> 1
		else -> funFib(i.dec()) + funFib(i.dec().dec())
	}

val fixFib = fix { i: Int ->
	when (i) {
		0 -> 0
		1 -> 1
		else -> this(i.dec()) + this(i.dec().dec())
	}
}

class FixTest {
	@Test
	fun fibo() {
		fixFib(10).assertEqualTo(55)
	}

	@Test
	fun performance() {
		repeat(10) {
			println("======")
			printTime("funFib: ") { funFib(36).assertEqualTo(14930352) }
			printTime("fixFib: ") { fixFib(36).assertEqualTo(14930352) }
		}
	}
}

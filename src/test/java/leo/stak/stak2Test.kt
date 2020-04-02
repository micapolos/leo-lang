package leo.stak

import leo.base.assertEqualTo
import leo.base.iterate
import leo.base.printTime
import org.junit.Test

val p0 = stak2("zero")
val p1 = stak2("one", p0)
val p2 = stak2("two", p1, p0)
val p3 = stak2("three", p2)
val p4 = stak2("four", p3, p2, p0)
val p5 = stak2("five", p4)
val p6 = stak2("six", p5, p4)
val p7 = stak2("seven", p6)
val p8 = stak2("eight", p7, p6, p4, p0)
val p9 = stak2("nine", p8)
val p10 = stak2("ten", p9, p8)

class Stak2Test {
	@Test
	fun payload() {
		p0.stakSize.assertEqualTo(1)
		p1.stakSize.assertEqualTo(2)
		p2.stakSize.assertEqualTo(3)
		p3.stakSize.assertEqualTo(4)
		p4.stakSize.assertEqualTo(5)
		p5.stakSize.assertEqualTo(6)
		p6.stakSize.assertEqualTo(7)
		p7.stakSize.assertEqualTo(8)
		p8.stakSize.assertEqualTo(9)
		p9.stakSize.assertEqualTo(10)
		p10.stakSize.assertEqualTo(11)

		p0.stakTop.assertEqualTo("zero")
		p1.stakTop.assertEqualTo("one")
		p2.stakTop.assertEqualTo("two")
		p3.stakTop.assertEqualTo("three")
		p4.stakTop.assertEqualTo("four")
		p5.stakTop.assertEqualTo("five")
		p6.stakTop.assertEqualTo("six")
		p7.stakTop.assertEqualTo("seven")
		p8.stakTop.assertEqualTo("eight")
		p9.stakTop.assertEqualTo("nine")
		p10.stakTop.assertEqualTo("ten")
	}

	@Test
	fun performance_1_vs_2() {
		var times = 100000000
		val a10 = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

		repeat(3) {
			printTime("Stak") {
				println("Stak size: " + 0.iterate(times) { plus(s10.size) })
			}

			printTime("Stak2") {
				println("Stak2 size: " + 0.iterate(times) { plus(p10.stakSize) })
			}

			printTime("Array") {
				println("Array size: " + 0.iterate(times) { plus(a10.size) })
			}
		}
	}
}
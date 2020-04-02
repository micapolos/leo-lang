package leo.stak

import leo.base.*
import leo13.Stack
import leo13.get
import leo13.push
import leo13.stack
import kotlin.random.Random
import kotlin.test.Test

class StakTest {
	@Test
	fun staks() {
		val s0 = stak("zero", null)
		val s1 = stak("one", link(s0, null))
		val s2 = stak("two", link(s1, link(s0, null)))
		val s3 = stak("three", link(s2, null))
		val s4 = stak("four", link(s3, link(s2, link(s0, null))))
		val s5 = stak("five", link(s4, null))
		val s6 = stak("six", link(s5, link(s4, null)))
		val s7 = stak("seven", link(s6, null))
		val s8 = stak("eight", link(s7, link(s6, link(s4, link(s0, null)))))
		val s9 = stak("nine", link(s8, null))
		val s10 = stak("ten", link(s9, link(s8, null)))

		val x = nullOf<Stak<String>>()
		val x0 = x.push("zero")
		val x1 = x0.push("one")
		val x2 = x1.push("two")
		val x3 = x2.push("three")
		val x4 = x3.push("four")
		val x5 = x4.push("five")
		val x6 = x5.push("six")
		val x7 = x6.push("seven")
		val x8 = x7.push("eight")
		val x9 = x8.push("nine")
		val x10 = x9.push("ten")

		x0.assertEqualTo(s0)
		x1.assertEqualTo(s1)
		x2.assertEqualTo(s2)
		x3.assertEqualTo(s3)
		x4.assertEqualTo(s4)
		x5.assertEqualTo(s5)
		x6.assertEqualTo(s6)
		x7.assertEqualTo(s7)
		x8.assertEqualTo(s8)
		x9.assertEqualTo(s9)
		x10.assertEqualTo(s10)

		x10.pop!!.assertEqualTo(x9)
		x10.pop!!.pop!!.assertEqualTo(x8)
		x10.pop!!.pop!!.pop!!.assertEqualTo(x7)
		x10.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x6)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x5)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x4)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x3)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x2)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x1)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.assertEqualTo(x0)
		x10.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop!!.pop.assertNull

		x10.pop(0).assertEqualTo(x10)
		x10.pop(1).assertEqualTo(x9)
		x10.pop(2).assertEqualTo(x8)
		x10.pop(3).assertEqualTo(x7)
		x10.pop(4).assertEqualTo(x6)
		x10.pop(5).assertEqualTo(x5)
		x10.pop(6).assertEqualTo(x4)
		x10.pop(7).assertEqualTo(x3)
		x10.pop(8).assertEqualTo(x2)
		x10.pop(9).assertEqualTo(x1)
		x10.pop(10).assertEqualTo(x0)
		x10.pop(11).assertNull
	}

	@Test
	fun large() {
		val size = 1000000
		val s = nullOf<Stak<Int>>().iterate(size) { push(123) }
		s?.get(size - 1).assertEqualTo(123)
		s?.get(size).assertNull
	}

	@Test
	fun performance() {
		val size = 1000000
		val access = 100

		repeat(10) {
			println("======")

			var stak0: Stak<Int>? = null
			var stack0: Stack<Int>? = null

			print("Create Stak: ")
			printTime {
				stak0 = nullOf<Stak<Int>>().iterate(size) { push(Random.nextInt()) }
			}

			print("Create Stack: ")
			printTime {
				stack0 = stack<Int>().iterate(size) { push(0) }
			}

			val stak = stak0!!
			print("Random access Stak: ")
			printTime {
				repeat(access) {
					stak.get(Random.nextInt(size * 20))
				}
			}

			val stack = stack0!!
			print("Random access Stack: ")
			printTime {
				repeat(access) {
					stack.get(Random.nextInt(size * 20))
				}
			}
		}
	}
}
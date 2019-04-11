package leo32.base

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ListTest {
	@Test
	fun size() {
		empty.list<Int>().size.assertEqualTo(0.i32)
		empty.list<Int>().add(1).size.assertEqualTo(1.i32)
		empty.list<Int>().add(1).add(2).size.assertEqualTo(2.i32)
		empty.list<Int>().add(1).add(2).drop.size.assertEqualTo(1.i32)
		empty.list<Int>().add(1).add(2).drop.dropFirst.size.assertEqualTo(0.i32)
	}

	@Test
	fun isEmpty() {
		empty.list<Int>().isEmpty.assertEqualTo(true)
		empty.list<Int>().add(1).isEmpty.assertEqualTo(false)
		empty.list<Int>().add(1).add(2).isEmpty.assertEqualTo(false)
		empty.list<Int>().add(1).add(2).drop.isEmpty.assertEqualTo(false)
		empty.list<Int>().add(1).add(2).drop.dropFirst.isEmpty.assertEqualTo(true)
	}

	@Test
	fun equality() {
		empty.list<Int>().assertEqualTo(empty.list())
		empty.list<Int>().add(1).assertEqualTo(empty.list<Int>().add(1))
		empty.list<Int>().add(1).add(2).assertEqualTo(empty.list<Int>().add(1).add(2))
		empty.list<Int>().add(1).add(2).drop.assertEqualTo(empty.list<Int>().add(1))
		empty.list<Int>().add(1).add(2).dropFirst.assertEqualTo(empty.list<Int>().add(2))
	}

	@Test
	fun seq() {
		empty.list<Int>().seq.assertContains()
		empty.list<Int>().add(1).seq.assertContains(1)
		empty.list<Int>().add(1).add(2).seq.assertContains(1, 2)
		empty.list<Int>().add(1).add(2).drop.seq.assertContains(1)
		empty.list<Int>().add(1).add(2).dropFirst.seq.assertContains(2)
		empty.list<Int>().add(1).add(2).dropFirst.dropFirst.seq.assertContains()
	}
}
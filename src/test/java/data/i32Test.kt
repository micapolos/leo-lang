package data

import leo.base.assertEqualTo
import kotlin.test.Test

class I32Test {
	@Test
	fun add() {
		val i1 = new.i1
		i1.i1Int.assertEqualTo(0)
		i1.i1Int = 1
		i1.i1Int.assertEqualTo(1)

		val i2 = new.i2
		i2.i2Int.assertEqualTo(0)
		i2.i2Int = 2
		i2.i2Int.assertEqualTo(2)

		val i32 = new.i32
		i32.i32Int.assertEqualTo(0)
		i32.i32Int = 13
		i32.i32Int.assertEqualTo(13)
		i32.i32Add(new.i32.apply { i32Int = 15 })
		i32.i32Int.assertEqualTo(28)
	}
}
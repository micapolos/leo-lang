package data

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun array8() {
		val a8 = new.array8 { new.i32 }
		a8.array8At(12).i32Int.assertEqualTo(0)
		a8.array8At(13).i32Int.assertEqualTo(0)
		a8.array8At(12).i32Int = 12
		a8.array8At(13).i32Int = 13
		a8.array8At(12).i32Int.assertEqualTo(12)
		a8.array8At(13).i32Int.assertEqualTo(13)
		a8.array8At(13).i32Add(a8.array8At(12))
		a8.array8At(13).i32Int.assertEqualTo(25)
	}
}
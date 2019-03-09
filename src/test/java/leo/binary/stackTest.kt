package leo.binary

import leo.base.assert
import leo.base.assertEqualTo
import kotlin.test.Test

class StackTest {
	@Test
	fun pushAndPop() {
		emptyStack32<Int>().top.assertEqualTo(null)
		emptyStack32<Int>().push(128)!!.top.assertEqualTo(128)
		emptyStack32<Int>().push(128)!!.push(256)!!.top.assertEqualTo(256)
		emptyStack32<Int>().push(128)!!.push(256)!!.pop!!.value.assertEqualTo(256)
		emptyStack32<Int>().push(128)!!.push(256)!!.pop!!.stack32.top.assertEqualTo(128)
		emptyStack32<Int>().push(128)!!.push(256)!!.pop!!.stack32.pop!!.value.assertEqualTo(128)
		emptyStack32<Int>().push(128)!!.push(256)!!.pop!!.stack32.pop!!.stack32.isEmpty
	}

	@Test
	fun performance() {
		var stack32 = emptyStack32<Int>()
		val size = 1 shl 20
		for (i in 0 until size) stack32 = stack32.push(i)!!
		stack32.top.assertEqualTo(size - 1)
		for (i in 0 until size) stack32 = stack32.pop!!.stack32
		stack32.isEmpty.assert
	}

}
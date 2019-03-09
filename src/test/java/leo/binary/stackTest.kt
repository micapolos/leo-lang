package leo.binary

import leo.base.assert
import leo.base.assertEqualTo
import kotlin.test.Test

class StackTest {
	@Test
	fun pushAndPop() {
		0.emptyStack32.top.assertEqualTo(null)
		0.emptyStack32.push(128)!!.top.assertEqualTo(128)
		0.emptyStack32.push(128)!!.push(256)!!.top.assertEqualTo(256)
		0.emptyStack32.push(128)!!.push(256)!!.pop!!.value.assertEqualTo(256)
		0.emptyStack32.push(128)!!.push(256)!!.pop!!.stack32.top.assertEqualTo(128)
		0.emptyStack32.push(128)!!.push(256)!!.pop!!.stack32.pop!!.value.assertEqualTo(128)
		0.emptyStack32.push(128)!!.push(256)!!.pop!!.stack32.pop!!.stack32.isEmpty
	}

	@Test
	fun performance() {
		var stack32 = 0.emptyStack32
		val size = 1 shl 20
		for (i in 0 until size) stack32 = stack32.push(i)!!
		stack32.top.assertEqualTo(size - 1)
		for (i in 0 until size) stack32 = stack32.pop!!.stack32
		stack32.isEmpty.assert
	}

}
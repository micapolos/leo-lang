package leo32.vm

import leo.base.assertEqualTo
import leo.base.fold
import kotlin.test.Test

class VmTest {
	@Test
	fun empty() {
		vm().assertInvokesTo()
	}

	@Test
	fun noOp() {
		vm(noOp)
			.assertInvokesTo()
	}

	@Test
	fun minus() {
		vm(constOp, 5, constOp, 3, minusOp)
			.assertInvokesTo(2)
	}

	@Test
	fun unaryMinus() {
		vm(constOp, 5, negOp)
			.assertInvokesTo(-5)
	}

	@Test
	fun jump() {
		vm(jumpOp, 2, constOp, 20, constOp, 10, noOp, noOp)
			.assertInvokesTo(10)
	}

	@Test
	fun loadStore() {
		vm(constOp, 50, constOp, 30, loadOp, 1, loadOp, 1, minusOp, storeOp, 1)
			.assertInvokesTo(20, 30)
	}

	@Test
	fun jumpZ_zero() {
		vm(constOp, 0, jumpZOp, 4, constOp, 10, jumpOp, 2, constOp, 20)
			.assertInvokesTo(20)
	}

	@Test
	fun jumpZ_nonZero() {
		vm(constOp, 100, jumpZOp, 4, constOp, 10, jumpOp, 2, constOp, 20)
			.assertInvokesTo(10)
	}

	@Test
	fun call() {
		vm(constOp, 50, constOp, 30, callOp, 2, 4, jumpOp, 3, minusOp, retOp, 0)
			.assertInvokesTo(20)
	}
}

fun Vm.assertInvokesTo(vararg ints: Int) =
	invoke().assertEqualTo(fold(ints.toTypedArray()) { push(it) }.pc(0))
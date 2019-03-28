package leo32.vm

import leo.base.assertEqualTo
import leo.base.fold
import kotlin.test.Test

class VmTest {
	@Test
	fun minus() {
		vm(constOp, 5, constOp, 3, minusOp, exitOp)
			.assertInvokesTo(2)
	}

	@Test
	fun unaryMinus() {
		vm(constOp, 5, negOp, exitOp)
			.assertInvokesTo(-5)
	}

	@Test
	fun jump() {
		vm(jumpOp, 4, noOp, noOp, exitOp)
			.assertInvokesTo()
	}

	@Test
	fun jumpIfZero_zero() {
		vm(constOp, 0, jumpIfZeroOp, 8, constOp, 10, jumpOp, 10, constOp, 20, exitOp)
			.assertInvokesTo(20)
	}

	@Test
	fun jumpIfZero_nonZero() {
		vm(constOp, 100, jumpIfZeroOp, 8, constOp, 10, jumpOp, 10, constOp, 20, exitOp)
			.assertInvokesTo(10)
	}

	@Test
	fun codegen() {
		vm(constOp, constOp, constOp, 10, constOp, negOp, constOp, exitOp)
			.invoke()
			.assertEqualTo(
				vm(constOp, constOp, constOp, 10, constOp, negOp, constOp, exitOp, constOp, 10, negOp, exitOp, -10).jump(12))
	}
}

fun Vm.assertInvokesTo(vararg ints: Int) =
	invoke()
		.assertEqualTo(fold(ints.toTypedArray()) { push(it) }.jump(sp.inc()))
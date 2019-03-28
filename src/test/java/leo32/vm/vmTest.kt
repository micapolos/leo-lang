package leo32.vm

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.fold
import kotlin.test.Test

class VmTest {
	@Test
	fun minus() {
		empty
			.vm
			.push(constOp, 5, constOp, 3, minusOp, exitOp)
			.assertInvokesTo(2)
	}

	@Test
	fun unaryMinus() {
		empty
			.vm
			.push(constOp, 5, negOp, exitOp)
			.assertInvokesTo(-5)
	}

	@Test
	fun jump() {
		empty
			.vm
			.push(jumpOp, 4, noOp, noOp, exitOp)
			.assertInvokesTo()
	}

	@Test
	fun jumpIfZero_zero() {
		empty
			.vm
			.push(constOp, 0, jumpIfZeroOp, 8, constOp, 10, jumpOp, 10, constOp, 20, exitOp)
			.assertInvokesTo(20)
	}

	@Test
	fun jumpIfZero_nonZero() {
		empty
			.vm
			.push(constOp, 100, jumpIfZeroOp, 8, constOp, 10, jumpOp, 10, constOp, 20, exitOp)
			.assertInvokesTo(10)
	}

	@Test
	fun call() {
		empty
			.vm
			.push(minusOp, jumpOp, 0, constOp, 5, constOp, 3, callOp, 0, 2, exitOp)
			.jump(3)
			.invoke()
			.assertEqualTo(
				empty
					.vm
					.push(minusOp, jumpOp, 10, constOp, 5, constOp, 3, callOp, 0, 2, exitOp, 2)
					.jump(11))
	}

	@Test
	fun codeGen() {
		empty.vm
			.push(constOp, constOp, constOp, 10, constOp, negOp, constOp, exitOp)
			.invoke()
			.assertEqualTo(
				empty
					.vm
					.push(constOp, constOp, constOp, 10, constOp, negOp, constOp, exitOp, constOp, 10, negOp, exitOp, -10)
					.jump(12))
	}
}

fun Vm.assertInvokesTo(vararg ints: Int) =
	invoke()
		.assertEqualTo(fold(ints.toTypedArray()) { push(it) }.jump(sp.inc()))
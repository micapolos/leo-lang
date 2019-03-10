package leo32

import leo.base.assertEqualTo
import leo.base.seqNode
import leo.binary.*
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke() {
		emptyFunction
			.invoke(zero.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime.push(zero.bit))
		emptyFunction
			.invoke(one.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime.push(one.bit))

		emptyFunction
			.define(zero.bit, push.op.match)
			.invoke(zero.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime
				.push(zero.bit))

		emptyFunction
			.define(zero.bit, push.op.match)
			.invoke(one.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime.push(one.bit))
	}

	@Test
	fun define() {
		emptyFunction
			.define(seqNode(zero.bit, zero.bit), "00".tag.log.op.match)
			.define(seqNode(zero.bit, one.bit), "01".tag.log.op.match)
			.define(seqNode(one.bit), "1".tag.log.op.match)
			.assertEqualTo(
				emptyFunction
					.put(zero.bit, emptyFunction
						.put(zero.bit, "00".tag.log.op.match)
						.put(one.bit, "01".tag.log.op.match)
						.match)
					.put(one.bit, "1".tag.log.op.match))
	}

	@Test
	fun nandInvoke() {
		nandFunction
			.runtime
			.invoke(zero.bit)
			.invoke(zero.bit)
			.bitStack
			.assertEqualTo(emptyStack32<Bit>().push(one.bit))
	}
}
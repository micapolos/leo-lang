package leo32

import leo.base.assertEqualTo
import leo.base.fold
import leo.base.seqNode
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke() {
		emptyFunction
			.invoke(zero.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime)
		emptyFunction
			.invoke(one.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime)

		emptyFunction
			.define(zero.bit, zero.bit.push.op.match)
			.invoke(zero.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime.push(zero.bit))

		emptyFunction
			.define(zero.bit, zero.bit.push.op.match)
			.invoke(one.bit, emptyRuntime)
			.assertEqualTo(emptyRuntime)
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
	fun booleanInvoke() {
		booleanFunction
			.runtime
			.fold("false".id.bitSeq) { invoke(it) }
			.assertEqualTo(booleanFunction.runtime.push(zero.bit))
	}
}
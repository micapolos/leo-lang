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
		emptyFunction<T>()
			.invoke(zero.bit, testRuntime.scope)
			.assertEqualTo(stackRuntime.scope)
		emptyFunction<T>()
			.invoke(one.bit, stackRuntime.scope)
			.assertEqualTo(stackRuntime.scope)

		emptyFunction<T>()
			.define(zero.bit, zero.bit.push.op<T>().match)
			.invoke(zero.bit, stackRuntime.scope)
			.assertEqualTo(stackRuntime.scope.push(zero.bit))

		emptyFunction<T>()
			.define(zero.bit, zero.bit.push.op<T>().match)
			.invoke(one.bit, stackRuntime.scope)
			.assertEqualTo(stackRuntime.scope)
	}

	@Test
	fun define() {
		emptyFunction<T>()
			.define(seqNode(zero.bit, zero.bit), "00".tag.log.op<T>().match)
			.define(seqNode(zero.bit, one.bit), "01".tag.log.op<T>().match)
			.define(seqNode(one.bit), "1".tag.log.op<T>().match)
			.assertEqualTo(
				emptyFunction<T>()
					.put(zero.bit, emptyFunction<T>()
						.put(zero.bit, "00".tag.log.op<T>().match)
						.put(one.bit, "01".tag.log.op<T>().match)
						.match)
					.put(one.bit, "1".tag.log.op<T>().match))
	}

	@Test
	fun booleanInvoke() {
		testRuntime
			.scope(booleanFunction())
			.fold("false".id.bitSeq) { invoke(it) }
			.assertEqualTo(testRuntime.scope(booleanFunction()).push(zero.bit))
	}
}
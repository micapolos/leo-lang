package leo32

import leo.base.assertEqualTo
import leo.base.nonEmptySequence
import leo.binary.oneBit
import leo.binary.zeroBit
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke() {
		emptyFunction
			.invoke(zeroBit, emptyRuntime)
			.assertEqualTo(null)
		emptyFunction
			.invoke(oneBit, emptyRuntime)
			.assertEqualTo(null)

		emptyFunction
			.define(zeroBit, push.op.match)
			.invoke(zeroBit, emptyRuntime)
			.assertEqualTo(emptyRuntime
				.push(zeroBit))

		emptyFunction
			.define(zeroBit, push.op.match)
			.invoke(oneBit, emptyRuntime)
			.assertEqualTo(null)
	}

	@Test
	fun define() {
		emptyFunction
			.define(nonEmptySequence(zeroBit, zeroBit), "00".tag.log.op.match)
			.define(nonEmptySequence(zeroBit, oneBit), "01".tag.log.op.match)
			.define(nonEmptySequence(oneBit), "1".tag.log.op.match)
			.assertEqualTo(
				emptyFunction
					.put(zeroBit, emptyFunction
						.put(zeroBit, "00".tag.log.op.match)
						.put(oneBit, "01".tag.log.op.match)
						.match)
					.put(oneBit, "1".tag.log.op.match))
	}
}
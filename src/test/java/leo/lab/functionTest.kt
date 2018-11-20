package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.bitStream
import kotlin.test.Test

class FunctionTest {
	val bBody = body(bWord.term(), emptyFunction)

	@Test
	fun get() {
		emptyFunction
			.define(aWord.term(), bBody)!!
			.get(Letter.A.bitStream)!!
			.functionOrNull!!
			.get(beginByte.bitStream)!!
			.functionOrNull!!
			.get(endByte.bitStream)!!
			.bodyOrNull
			.assertEqualTo(bBody)
	}

	@Test
	fun defineAndInvoke() {
		emptyFunction
			.define(aWord.term(), bBody)!!
			.invoke(aWord.term())
			.assertEqualTo(bWord.term)
	}
}
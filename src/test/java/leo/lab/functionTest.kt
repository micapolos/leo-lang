package leo.lab

import leo.*
import leo.base.assertEqualTo
import leo.base.bitStream
import kotlin.test.Test

class FunctionTest {
	val bBody = body(bWord.term(), emptyFunction)
	val stringBody = body(stringWord.term(), emptyFunction)
	val numberBody = body(numberWord.term(), emptyFunction)

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
	fun defineSimpleAndInvoke() {
		emptyFunction
			.define(aWord.term(), bBody)!!
			.invoke(aWord.term())
			.assertEqualTo(bWord.term)
	}

	@Test
	fun defineTwo_getFirst() {
		emptyFunction
			.define(ageWord.term(), body(numberWord.term(), emptyFunction))!!
			.define(nameWord.term(), body(stringWord.term(), emptyFunction))!!
			.invoke(ageWord.term)
			.assertEqualTo(numberWord.term)
	}

	@Test
	fun defineTwo_getSecond() {
		emptyFunction
			.define(ageWord.term(), body(numberWord.term(), emptyFunction))!!
			.define(nameWord.term(), body(stringWord.term(), emptyFunction))!!
			.invoke(nameWord.term)
			.assertEqualTo(stringWord.term)
	}

	@Test
	fun defineCommonPrefix_getFirst() {
		emptyFunction
			.define(bodyWord.term(), body(oneWord.term(), emptyFunction))!!
			.define(booleanWord.term(), body(twoWord.term(), emptyFunction))!!
			.invoke(bodyWord.term)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun defineCommonPrefix_getSecond() {
		emptyFunction
			.define(bodyWord.term(), body(oneWord.term(), emptyFunction))!!
			.define(booleanWord.term(), body(twoWord.term(), emptyFunction))!!
			.invoke(booleanWord.term)
			.assertEqualTo(twoWord.term)
	}

	@Test
	fun redefineSame() {
		emptyFunction
			.define(aWord.term(), bBody)!!
			.define(aWord.term(), bBody)
			.assertEqualTo(null)
	}

	@Test
	fun redefineLonger() {
		emptyFunction
			.define(term(aWord.field), body(oneWord.term(), emptyFunction))!!
			.define(term(aWord.field, bWord.field), body(twoWord.term(), emptyFunction))
			.assertEqualTo(null)
	}

	@Test
	fun redefineShorter() {
		emptyFunction
			.define(term(aWord.field, bWord.field), body(twoWord.term(), emptyFunction))!!
			.define(term(aWord.field), body(oneWord.term(), emptyFunction))
			.assertEqualTo(null)
	}

	@Test
	fun defineWithoutAndWithRhs_invokeWithoutRhs() {
		emptyFunction
			.define(term(aWord.field), body(oneWord.term(), emptyFunction))!!
			.define(term(aWord fieldTo bWord.term), body(twoWord.term(), emptyFunction))!!
			.invoke(aWord.term)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun defineWithoutAndWithRhs_invokeWithRhs() {
		emptyFunction
			.define(term(aWord.field), body(oneWord.term(), emptyFunction))!!
			.define(term(aWord fieldTo bWord.term), body(twoWord.term(), emptyFunction))!!
			.invoke(term(aWord fieldTo bWord.term))
			.assertEqualTo(twoWord.term)
	}

	@Test
	fun defineOneOf_invokeFirst() {
		emptyFunction
			.define(pattern(aWord.term(), bWord.term()).metaTerm, body(oneWord.term(), emptyFunction))!!
			.invoke(aWord.term)
			.assertEqualTo(oneWord.term)
	}

	@Test
	fun defineOneOf_invokeSecond() {
		emptyFunction
			.define(pattern(aWord.term(), bWord.term()).metaTerm, body(oneWord.term(), emptyFunction))!!
			.invoke(bWord.term)
			.assertEqualTo(oneWord.term)
	}
}
package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class OpTest {
	@Test
	fun switch() {
		val switch = switch(
			term("true") caseTo term("false"),
			term("false") caseTo term("true"))

		op(switch).invoke(term("true"), parameter(term())).assertEqualTo(term("false"))
		op(switch).invoke(term("false"), parameter(term())).assertEqualTo(term("true"))
	}

//	@Test
//	fun template() {
//		term(
//			"foo" fieldTo term("foo1"),
//			"bar" fieldTo term("bar1"))
//			.invoke(template(selector("foo".getter)).getter)
//			.assertEqualTo(term("foo1"))
//
//		term(
//			"foo" fieldTo term("foo1"),
//			"bar" fieldTo term("bar1"))
//			.invoke(template(selector("bar".getter)).getter)
//			.assertEqualTo(term("bar1"))
//	}

}
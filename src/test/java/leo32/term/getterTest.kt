package leo32.term

import leo.base.assertEqualTo
import leo.base.empty
import leo32.base.i32
import kotlin.test.Test

class GetterTest {
	@Test
	fun invoke() {
		val term = term(
			"foo" fieldTo term("foo1"),
			"bar" fieldTo term("bar1"))

		term.invoke("foo".getter).assertEqualTo(term("foo1"))
		term.invoke("bar".getter).assertEqualTo(term("bar1"))
		term.invoke(0.i32.getter).assertEqualTo(term("foo1"))
		term.invoke(1.i32.getter).assertEqualTo(term("bar1"))
	}

	@Test
	fun switch() {
		val switch = empty.switch
			.put(term("true"), term("false"))
			.put(term("false"), term("true"))
		
		term("true").invoke(switch.getter).assertEqualTo(term("false"))
		term("false").invoke(switch.getter).assertEqualTo(term("true"))
	}

	@Test
	fun template() {
		term(
			"foo" fieldTo term("foo1"),
			"bar" fieldTo term("bar1"))
			.invoke(template(selector("foo".getter)).getter)
			.assertEqualTo(term("foo1"))

		term(
			"foo" fieldTo term("foo1"),
			"bar" fieldTo term("bar1"))
			.invoke(template(selector("bar".getter)).getter)
			.assertEqualTo(term("bar1"))
	}
}
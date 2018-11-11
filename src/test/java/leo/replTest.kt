package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class ReplTest {
	@Test
	fun data() {
		emptyRepl
			.push("one(number)")
			?.evaluatedScript
			.assertEqualTo(script(term(oneWord fieldTo term(numberWord))))
	}

	@Test
	fun rule() {
		emptyRepl
			.push("define(it(two)is(number))it(two)")
			?.evaluatedScript
			.assertEqualTo(script(term(itWord fieldTo term(numberWord))))
	}

	@Test
	fun reader() {
		emptyRepl
			.push("define(it(leo(reader)read(byte(bit(zero)bit(zero)bit(zero)bit(zero)bit(one)bit(zero)bit(one)bit(zero))))is(leo(reader)))\n")
			.assertEqualTo(script(term(itWord fieldTo term(oneWord))))
	}
}
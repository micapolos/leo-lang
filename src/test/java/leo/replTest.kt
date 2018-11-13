package leo

import leo.base.assertEqualTo
import kotlin.test.Test

class ReplTest {
	@Test
	fun data() {
		emptyRepl
			.push("one(number)")
			?.evaluatedValueTerm
			.assertEqualTo(term(oneWord fieldTo term(numberWord)))
	}

	@Test
	fun rule() {
		emptyRepl
			.push("define(it(two)is(number))it(two)")
			?.evaluatedValueTerm
			.assertEqualTo(term(itWord fieldTo term(numberWord)))
	}

	@Test
	fun reader() {
		emptyRepl
			.push("define(it(leo(reader)read(byte(bit(zero)bit(zero)bit(zero)bit(zero)bit(one)bit(zero)bit(one)bit(zero))))is(leo(reader)))\nit(\none\n)")
			?.evaluatedValueTerm
			.assertEqualTo(term(itWord fieldTo term(oneWord)))
	}
}
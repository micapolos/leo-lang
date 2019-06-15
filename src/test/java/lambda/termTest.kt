package lambda

import leo.base.assertEqualTo
import org.junit.Test

class TermTest {
	val falseTerm get() = term { x -> term { y -> x } }
	val trueTerm get() = term { x -> term { y -> y } }
	val notTerm get() = term { b -> b.apply(trueTerm).apply(falseTerm) }

	@Test
	fun boolean() {
		falseTerm.eq(falseTerm).assertEqualTo(true)
		falseTerm.eq(trueTerm).assertEqualTo(false)
		trueTerm.eq(falseTerm).assertEqualTo(false)
		trueTerm.eq(trueTerm).assertEqualTo(true)

		falseTerm.reduce.assertEqualTo(falseTerm)
		trueTerm.reduce.assertEqualTo(trueTerm)
		notTerm.reduce.assertEqualTo(notTerm)

		notTerm.invoke(falseTerm).assertEqualTo(trueTerm)
		notTerm.invoke(trueTerm).assertEqualTo(falseTerm)
		notTerm.invoke(notTerm.invoke(falseTerm)).assertEqualTo(falseTerm)
		notTerm.invoke(notTerm.invoke(trueTerm)).assertEqualTo(trueTerm)
	}
}
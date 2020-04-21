package leo15.lambda

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.indexed
import leo13.assertContains
import leo13.push
import leo13.stack
import kotlin.test.Test

class UtilTest {
	@Test
	fun pairUnpair() {
		pairTerm
			.invoke("Hello, ".valueTerm)
			.invoke("world!".valueTerm)
			.unsafeUnpair
			.assertEqualTo("Hello, ".valueTerm to "world!".valueTerm)
	}

	@Test
	fun freeVariableCount() {
		at(0).freeVariableCount.assertEqualTo(1)
		at(1).freeVariableCount.assertEqualTo(2)
		at(2).freeVariableCount.assertEqualTo(3)
		fn(at(0)).freeVariableCount.assertEqualTo(0)
		fn(at(1)).freeVariableCount.assertEqualTo(1)
		fn(at(2)).freeVariableCount.assertEqualTo(2)
		fn(fn(at(2))).freeVariableCount.assertEqualTo(1)
		fn(fn(fn(at(2)))).freeVariableCount.assertEqualTo(0)
		at(0)(at(0)).freeVariableCount.assertEqualTo(1)
		at(0)(at(1)).freeVariableCount.assertEqualTo(2)
		at(1)(at(0)).freeVariableCount.assertEqualTo(2)
		at(1)(at(1)).freeVariableCount.assertEqualTo(2)
	}

	@Test
	fun const_() {
		constTerm
			.invoke(10.valueTerm)
			.invoke("foo".valueTerm)
			.eval
			.assertEqualTo(10.valueTerm)
	}

	@Test
	fun choice() {
		choiceTerm(3, 0, 10.valueTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.eval
			.assertEqualTo(10.valueTerm)

		choiceTerm(3, 1, 10.valueTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.eval
			.assertEqualTo(10.valueTerm)

		choiceTerm(3, 2, 10.valueTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.invoke(idTerm)
			.eval
			.assertEqualTo(10.valueTerm)

		choiceTerm(3, 0, 10.valueTerm)
			.invoke(constTerm("zero".valueTerm))
			.invoke(constTerm("one".valueTerm))
			.invoke(constTerm("two".valueTerm))
			.eval
			.assertEqualTo("two".valueTerm)

		choiceTerm(3, 1, 10.valueTerm)
			.invoke(constTerm("zero".valueTerm))
			.invoke(constTerm("one".valueTerm))
			.invoke(constTerm("two".valueTerm))
			.eval
			.assertEqualTo("one".valueTerm)

		choiceTerm(3, 2, 10.valueTerm)
			.invoke(constTerm("zero".valueTerm))
			.invoke(constTerm("one".valueTerm))
			.invoke(constTerm("two".valueTerm))
			.eval
			.assertEqualTo("zero".valueTerm)
	}

	@Test
	fun unsafeUnchoice() {
		choiceTerm(3, 2, 10.valueTerm)
			.unsafeUnchoice(3)
			.assertEqualTo(2 indexed 10.valueTerm)
	}

	@Test
	fun unsafeFold() {
		stack<Term>()
			.unsafeFold(idTerm.append(10.valueTerm).append(20.valueTerm).append(30.valueTerm)) { push(it) }
			.assertContains(30.valueTerm, 20.valueTerm, 10.valueTerm)
	}

	@Test
	fun applicationSeqNode() {
		10.valueTerm
			.applicationSeqNode
			.assertContains(10.valueTerm)

		10.valueTerm
			.invoke(20.valueTerm)
			.applicationSeqNode
			.assertContains(20.valueTerm, 10.valueTerm)

		10.valueTerm
			.invoke(20.valueTerm)
			.invoke(30.valueTerm)
			.applicationSeqNode
			.assertContains(30.valueTerm, 20.valueTerm, 10.valueTerm)

	}
}
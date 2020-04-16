package leo15.type

import leo.base.assertEqualTo
import leo15.lambda.append
import leo15.lambda.firstTerm
import leo15.lambda.invoke
import leo15.lambda.secondTerm
import kotlin.test.Test

class TermsTest {
	@Test
	fun plus() {
		add(10.term, false, 20.term, false).assertEqualTo(10.term.append(20.term))
		add(10.term, false, 20.term, true).assertEqualTo(10.term)
		add(10.term, true, 20.term, false).assertEqualTo(20.term)
		add(10.term, true, 20.term, true).assertEqualTo(emptyTerm)
	}

	@Test
	fun unplus() {
		val term = 10.term.append(20.term)
		term.unplus(false, false).assertEqualTo(term.invoke(firstTerm) to term.invoke(secondTerm))
		term.unplus(false, true).assertEqualTo(term to emptyTerm)
		term.unplus(true, false).assertEqualTo(emptyTerm to term)
		term.unplus(true, true).assertEqualTo(emptyTerm to emptyTerm)
	}

	@Test
	fun decompileUnplus() {
		val term = 10.term.append(20.term)
		term.decompileUnplus(false, false).assertEqualTo(10.term to 20.term)
		term.decompileUnplus(false, true).assertEqualTo(term to emptyTerm)
		term.decompileUnplus(true, false).assertEqualTo(emptyTerm to term)
		term.decompileUnplus(true, true).assertEqualTo(emptyTerm to emptyTerm)
	}
}
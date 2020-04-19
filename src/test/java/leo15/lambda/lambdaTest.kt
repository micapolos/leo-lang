package leo15.lambda

import leo.base.assertEqualTo
import leo15.terms.intMinus
import leo15.terms.intPlus
import leo15.terms.term
import org.junit.Test

class LambdaTest {
	@Test
	fun lambda_() {
		lambda { x -> x.invoke(x) }
			.assertEqualTo(fn(lambdaVar(0).invoke(lambdaVar(0))))

		lambda { x -> x.invoke(x) }
			.resolveVars
			.assertEqualTo(fn(at(0).invoke(at(0))))

		lambda { x -> x.intPlus(x) }.invoke(10.term)
			.eval
			.assertEqualTo(20.term)

		lambda { x -> lambda { y -> x.invoke(y) } }
			.assertEqualTo(fn(fn(lambdaVar(0).invoke(lambdaVar(1)))))

		lambda { x -> lambda { y -> x.invoke(y) } }
			.resolveVars
			.assertEqualTo(fn(fn(at(1).invoke(at(0)))))

		lambda { x -> lambda { y -> x.intMinus(y) } }
			.invoke(5.term)
			.invoke(3.term)
			.eval
			.assertEqualTo(2.term)
	}
}
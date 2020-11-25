package leo21.value

import leo.base.assertEqualTo
import leo14.lambda.first
import leo14.lambda.fix
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.pair
import leo14.lambda.second
import leo14.lambda.switch
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.prim
import leo21.prim.runtime.value
import leo21.term.arg
import leo21.term.numberEqualsNumber
import leo21.term.numberMinusNumber
import leo21.term.numberPlusNumber
import leo21.term.numberString
import leo21.term.stringPlusString
import leo21.term.term
import kotlin.test.Test

class ValueTest {
	@Test
	fun numberPlusNumber() {
		term(10)
			.numberPlusNumber(term(20))
			.value
			.assertEqualTo(value(prim(30)))
	}

	@Test
	fun numberEqualsNumber_yes() {
		term(10)
			.numberEqualsNumber(term(10))
			.switch(fn(term("yes")), fn(term("no")))
			.value
			.assertEqualTo(value(prim("yes")))
	}

	@Test
	fun numberEqualsNumber_no() {
		term(10)
			.numberEqualsNumber(term(20))
			.switch(fn(term("yes")), fn(term("no")))
			.value
			.assertEqualTo(value(prim("no")))
	}

	@Test
	fun countdown() {
		fix<Prim>()
			.invoke(
				fn(
					fn(
						arg(0)
							.second
							.numberEqualsNumber(term(0))
							.switch(
								fn(
									arg(1)
										.first
										.stringPlusString(term("GO!!!"))),
								fn(
									arg(2)
										.invoke(
											pair(
												arg(1)
													.first
													.stringPlusString(arg(1).second.numberString)
													.stringPlusString(term(", ")),
												arg(1).second.numberMinusNumber(term(1)))))))))
			.invoke(pair(term("Countdown: "), term(3)))
			.value
			.assertEqualTo(value(prim("Countdown: 3, 2, 1, GO!!!")))
	}
}
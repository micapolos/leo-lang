package leo21.value

import leo.base.assertEqualTo
import leo14.lambda.first
import leo14.lambda.fix
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.pair
import leo14.lambda.second
import leo14.lambda.switch
import leo14.lambda.syntax.eitherSwitch
import leo14.lambda.syntax.invoke
import leo14.lambda.syntax.pairFirst
import leo14.lambda.syntax.pairSecond
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.prim
import leo21.prim.runtime.value
import leo21.syntax.fn
import leo21.syntax.number
import leo21.syntax.numberEqualsNumber
import leo21.syntax.numberMinusNumber
import leo21.syntax.numberPlusNumber
import leo21.syntax.numberText
import leo21.syntax.pairTo
import leo21.syntax.recFn
import leo21.syntax.text
import leo21.syntax.textPlusText
import leo21.syntax.value
import leo21.term.arg
import leo21.term.numberEqualsNumber
import leo21.term.numberMinusNumber
import leo21.term.numberString
import leo21.term.stringPlusString
import leo21.term.term
import kotlin.test.Test

class ValueTest {
	@Test
	fun numberText() {
		number(10)
			.numberText
			.value
			.assertEqualTo(value(prim("10")))
	}

	@Test
	fun numberPlusNumber() {
		number(10)
			.numberPlusNumber(number(20))
			.value
			.assertEqualTo(value(prim(30)))
	}

	@Test
	fun numberEqualsNumber_yes() {
		number(10)
			.numberEqualsNumber(number(10))
			.eitherSwitch(
				fn { text("yes") },
				fn { text("no") })
			.value
			.assertEqualTo(value(prim("yes")))
	}

	@Test
	fun numberEqualsNumber_no() {
		number(10)
			.numberEqualsNumber(number(20))
			.eitherSwitch(
				fn { text("yes") },
				fn { text("no") })
			.value
			.assertEqualTo(value(prim("no")))
	}

	@Test
	fun countdown2() {
		recFn { countdownFn, textPairToNumber ->
			textPairToNumber
				.pairSecond
				.numberEqualsNumber(number(0))
				.eitherSwitch(
					fn {
						textPairToNumber
							.pairFirst
							.textPlusText(text("GO!!!"))
					},
					fn {
						countdownFn
							.invoke(
								textPairToNumber
									.pairFirst
									.textPlusText(textPairToNumber.pairSecond.numberText)
									.textPlusText(text(", "))
									.pairTo(textPairToNumber.pairSecond.numberMinusNumber(number(1))))
					})
		}
			.invoke(text("Countdown: ").pairTo(number(3)))
			.value
			.assertEqualTo(value(prim("Countdown: 3, 2, 1, GO!!!")))
	}
}
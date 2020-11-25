package leo21.syntax

import leo14.lambda.syntax.eitherSwitch
import leo14.lambda.syntax.invoke
import leo14.lambda.syntax.pairFirst
import leo14.lambda.syntax.pairSecond

val X.countdown: X
	get() =
		recFn { countdownFn, textPairToNumber ->
			textPairToNumber
				.pairSecond
				.numberEqualsNumber(number(0))
				.eitherSwitch(
					{
						textPairToNumber
							.pairFirst
							.textPlusText(text("GO!!!"))
					},
					{
						countdownFn
							.invoke(
								textPairToNumber
									.pairFirst
									.textPlusText(textPairToNumber.pairSecond.numberText)
									.textPlusText(text(", "))
									.pairTo(textPairToNumber.pairSecond.numberMinusNumber(number(1))))
					})
		}.invoke(text("Countdown: ").pairTo(this))

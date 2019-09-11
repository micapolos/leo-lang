package leo13

import leo.base.assertEqualTo
import leo.base.nullOf
import org.junit.Test

class WordSwitchTest {
	@Test
	fun plusResult() {
		nullOf<WordSwitch>()
			.orNullPlusResult(case(zeroWord))
			.assertEqualTo(successResult(uncheckedSwitch(case(zeroWord))))

		uncheckedSwitch(case(zeroWord), case(oneWord))
			.orNullPlusResult(case(twoWord))
			.assertEqualTo(successResult(uncheckedSwitch(case(zeroWord), case(oneWord), case(twoWord))))

		uncheckedSwitch(case(zeroWord), case(oneWord))
			.orNullPlusResult(case(oneWord))
			.assertEqualTo(failureResult(duplicate(zeroWord)))
	}

	@Test
	fun sentenceLine() {
		switch(case(zeroWord), case(oneWord))
			.sentenceLine
			.assertEqualTo(
				switchWord lineTo sentence(
					case(zeroWord).sentenceLine,
					case(oneWord).sentenceLine))
	}
}
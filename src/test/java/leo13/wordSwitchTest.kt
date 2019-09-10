package leo13

import leo.base.assertEqualTo
import org.junit.Test

class WordSwitchTest {
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
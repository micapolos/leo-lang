package leo.base

import kotlin.test.Test

class BitPatternTest {
	@Test
	fun chainRecurse() {
		val bitPattern =
			bitPattern(
				bitPatternMatch("value"),
				bitPatternRecurseMatch(0))

		bitPattern.pushParser.push(0.bit)?.parsedOrNull?.assertEqualTo("value")
		bitPattern.pushParser.push(1.bit)?.parsedOrNull?.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(0.bit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.bit)?.push(1.bit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(1.bit)?.push(0.bit)?.parsedOrNull.assertEqualTo("value")
	}

	@Test
	fun treeRecurse() {
		val bitPattern =
			bitPattern(
				bitPatternMatch("value"),
				match(
					bitPattern(
						bitPatternRecurseMatch(1),
						bitPatternRecurseMatch(1))))

		bitPattern.pushParser.push(0.bit)?.parsedOrNull?.assertEqualTo("value")
		bitPattern.pushParser.push(1.bit)?.parsedOrNull?.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(0.bit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(1.bit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(0.bit)?.push(0.bit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.bit)?.push(0.bit)?.push(1.bit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.bit)?.push(1.bit)?.push(0.bit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.bit)?.push(1.bit)?.push(1.bit)?.parsedOrNull.assertEqualTo(null)
	}
}
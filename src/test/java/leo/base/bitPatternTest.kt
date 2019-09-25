package leo.base

import kotlin.test.Test

class BitTypeTest {
	@Test
	fun chainRecurse() {
		val bitPattern =
			bitPattern(
				bitPatternMatch("value"),
				bitPatternRecurseMatch(0))

		bitPattern.pushParser.push(0.enumBit)?.parsedOrNull?.assertEqualTo("value")
		bitPattern.pushParser.push(1.enumBit)?.parsedOrNull?.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo("value")
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

		bitPattern.pushParser.push(0.enumBit)?.parsedOrNull?.assertEqualTo("value")
		bitPattern.pushParser.push(1.enumBit)?.parsedOrNull?.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(0.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.enumBit)?.push(0.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		bitPattern.pushParser.push(1.enumBit)?.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo("value")
		bitPattern.pushParser.push(1.enumBit)?.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
	}
}
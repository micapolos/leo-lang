package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun wordPattern() {
		val trueOrFalsePattern = pattern(
			trueWord to pattern()
				.end(resolution(match(template(oneWord.script)))),
			falseWord to pattern()
				.end(resolution(match(template(twoWord.script)))))

		trueOrFalsePattern.invoke(trueWord.script).assertEqualTo(oneWord.script)
		trueOrFalsePattern.invoke(falseWord.script).assertEqualTo(twoWord.script)
		trueOrFalsePattern.invoke(zeroWord.script).assertEqualTo(zeroWord.script)
	}

	@Test
	fun rhsPattern() {
		val trueOrFalsePattern = pattern(
			booleanWord to pattern(
				trueWord to pattern()
					.end(resolution(match(pattern()
						.end(resolution(match(template(oneWord.script))))))),
				falseWord to pattern()
					.end(resolution(match(pattern()
						.end(resolution(match(template(twoWord.script)))))))))

		trueOrFalsePattern
			.invoke(script(booleanWord to trueWord.script))
			.assertEqualTo(oneWord.script)
		trueOrFalsePattern
			.invoke(script(booleanWord to falseWord.script))
			.assertEqualTo(twoWord.script)
		trueOrFalsePattern
			.invoke(script(booleanWord to oneWord.script))
			.assertEqualTo(script(booleanWord to oneWord.script))
		trueOrFalsePattern
			.invoke(booleanWord.script)
			.assertEqualTo(booleanWord.script)
	}

	@Test
	fun lhsPattern() {
		val trueOrFalsePattern = pattern(
			trueWord to pattern()
				.end(resolution(match(pattern(
					negateWord to pattern()
						.end(resolution(match(template(oneWord.script)))))))),
			falseWord to pattern()
				.end(resolution(match(pattern(
					negateWord to pattern()
						.end(resolution(match(template(twoWord.script)))))))))

		trueOrFalsePattern
			.invoke(script(trueWord to null, negateWord to null))
			.assertEqualTo(oneWord.script)
		trueOrFalsePattern
			.invoke(script(falseWord to null, negateWord to null))
			.assertEqualTo(twoWord.script)
		trueOrFalsePattern
			.invoke(trueWord.script)
			.assertEqualTo(trueWord.script)
	}
}
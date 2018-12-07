package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun wordPattern() {
		val trueOrFalsePattern = pattern(
			patternMap(
				trueWord to pattern(
					resolution(
						match(
							template(oneWord.script)))),
				falseWord to pattern(
					resolution(
						match(
							template(twoWord.script))))))

		trueOrFalsePattern.invoke(trueWord.script).assertEqualTo(oneWord.script)
		trueOrFalsePattern.invoke(falseWord.script).assertEqualTo(twoWord.script)
		trueOrFalsePattern.invoke(zeroWord.script).assertEqualTo(zeroWord.script)
	}

	@Test
	fun rhsPattern() {
		val trueOrFalsePattern = pattern(
			patternMap(
				booleanWord to pattern(
					patternMap(
						trueWord to pattern(
							resolution(
								match(
									pattern(
										resolution(
											match(
												template(oneWord.script))))))),
						falseWord to pattern(
							resolution(
								match(
									pattern(
										resolution(
											match(
												template(twoWord.script)))))))))))

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
			patternMap(
				trueWord to pattern(
					resolution(
						match(
							pattern(
								patternMap(
									negateWord to pattern(
										resolution(
											match(
												template(oneWord.script))))))))),
				falseWord to pattern(
					resolution(
						match(
							pattern(
								patternMap(
									negateWord to pattern(
										resolution(
											match(
												template(twoWord.script)))))))))))

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
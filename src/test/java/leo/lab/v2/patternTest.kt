package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun wordPattern() {
		val trueOrFalsePattern = pattern(
			trueWord caseTo pattern(
				end caseTo match(
					template(oneWord.script))),
			falseWord caseTo pattern(
				end caseTo match(
					template(twoWord.script))))

		trueOrFalsePattern.invoke(trueWord.script).assertEqualTo(oneWord.script)
		trueOrFalsePattern.invoke(falseWord.script).assertEqualTo(twoWord.script)
		trueOrFalsePattern.invoke(zeroWord.script).assertEqualTo(zeroWord.script)
	}

	@Test
	fun rhsPattern() {
		val trueOrFalsePattern = pattern(
			booleanWord caseTo pattern(
				trueWord caseTo pattern(
					end caseTo match(
						pattern(
							end caseTo match(
								template(oneWord.script))))),
				falseWord caseTo pattern(
					end caseTo match(
						pattern(
							end caseTo match(
								template(twoWord.script)))))))

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
			trueWord caseTo pattern(
				end caseTo match(
					pattern(
						negateWord caseTo pattern(
							end caseTo match(
								template(oneWord.script)))))),
			falseWord caseTo pattern(
				end caseTo match(
					pattern(
						negateWord caseTo pattern(
							end caseTo match(
								template(twoWord.script)))))))

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

	@Test
	fun recursionPattern() {
		val naturalPattern = pattern(
			zeroWord caseTo pattern(
				end caseTo match(
					template(numberWord.script))),
			incrementWord caseTo pattern(
				end caseTo match(
					pattern(nullRecursion.previous))))

		naturalPattern
			.invoke(zeroWord.script)
			.assertEqualTo(numberWord.script)

		naturalPattern
			.invoke(script(incrementWord to zeroWord.script))
			.assertEqualTo(numberWord)
	}
}
package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	private val trueOrFalsePattern = pattern(
		trueWord to pattern()
			.end(resolution(match(template(oneWord.script)))),
		falseWord to pattern()
			.end(resolution(match(template(twoWord.script)))))

	@Test
	fun wordBeginCommand() {
		match(trueOrFalsePattern)
			.resolver
			.invoke(begin.command(trueWord))
			.assertEqualTo(match(pattern().end(resolution(match(template(oneWord.script))))).resolver)
	}

	@Test
	fun wordBeginEndCommand() {
		match(trueOrFalsePattern)
			.resolver
			.invoke(begin.command(trueWord))!!
			.invoke(end.command)
			.assertEqualTo(match(template(oneWord.script)).resolver)
	}
}
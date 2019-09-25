package leo13.compiler

import leo13.assertContains
import leo13.processorStack
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class ScriptCompilerTest {
	@Test
	fun processName() {
		processorStack<Script> {
			scriptTokenProcessor
				.process(token(opening("point")))
				.process(token(closing))
		}.assertContains(script("point"))
	}

	@Test
	fun processLine() {
		processorStack<Script> {
			scriptTokenProcessor
				.process(token(opening("point")))
				.process(token(opening("zero")))
				.process(token(closing))
				.process(token(closing))
		}.assertContains(script("point" lineTo script("zero")))
	}

	@Test
	fun processLines() {
		processorStack<Script> {
			scriptTokenProcessor
				.process(token(opening("x")))
				.process(token(opening("zero")))
				.process(token(closing))
				.process(token(closing))
				.process(token(opening("y")))
				.process(token(opening("one")))
				.process(token(closing))
				.process(token(closing))
		}.assertContains(
			script("x" lineTo script("zero")),
			script("x" lineTo script("zero"), "y" lineTo script("one")))
	}
}
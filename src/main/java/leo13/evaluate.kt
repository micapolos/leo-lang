package leo13

import leo.base.Seq
import leo.base.seq
import leo.base.thenFn
import leo13.interpreter.interpreted
import leo13.interpreter.interpreter
import leo13.locator.locator
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.tokenizer.tokenizer
import leo13.value.scriptOrNull

val Seq<Char>.charEvaluateScriptLine: ScriptLine
	get() =
		processorUpdate(interpreted()) {
			traced {
				interpreter()
					.tokenizer()
					.locator()
					.process(thenFn { seq(endOfTransmissionChar) })
			}.onError {
				errorName lineTo this
			}
		}.let { interpreted ->
			okName lineTo interpreted.typed.value.scriptOrNull!!
		}

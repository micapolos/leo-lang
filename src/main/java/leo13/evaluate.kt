package leo13

import leo.base.Seq
import leo.base.seq
import leo.base.thenFn
import leo13.compiler.ExpressionTyped
import leo13.compiler.compiler
import leo13.expression.valueContext
import leo13.locator.locator
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.token.Token
import leo13.tokenizer.tokenizer
import leo13.value.scriptOrNull
import leo13.value.value

val Seq<Char>.charEvaluateScriptLine: ScriptLine
	get() =
		converterCapture<ExpressionTyped, Token> {
			traced {
				compiler().tokenizer().locator().process(thenFn { seq(endOfTransmissionChar) })
			}.onError {
				errorName lineTo this
			}
		}.let { compiled ->
			okName lineTo valueContext().give(value()).evaluate(compiled.expression).scriptOrNull!!
		}

package leo21.compiled

import leo14.ScriptError
import leo14.anyReflectScriptLine
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.StringTryNumberPrim
import leo21.token.processor.staticCompiled
import leo21.type.numberType
import leo21.type.try_

fun Compiled.try_(fn: Compiled.() -> Compiled): Compiled =
	compiled(
		"try" lineTo compiled(
			try {
				"success" lineTo fn()
			} catch (scriptError: ScriptError) {
				"error" lineTo scriptError.script.staticCompiled
			} catch (throwable: Throwable) {
				"error" lineTo compiled(throwable.anyReflectScriptLine.staticCompiled)
			}
		)
	)

val Compiled.stringTryNumber: Compiled
	get() =
		nativeTerm(StringTryNumberPrim)
			.invoke(term)
			.of(numberType.try_)
package leo21.compiled

import leo14.ScriptError
import leo14.anyReflectScriptLine
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.StringTryNumberPrim
import leo21.token.processor.staticCompiled
import leo21.type.choice
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberType
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.stringType
import leo21.type.type

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
		nativeTerm(StringTryNumberPrim).invoke(term)
			.of(
				type(
					"try" lineTo type(
						line(
							choice(
								"success" lineTo numberType,
								"error" lineTo type(
									stringLine,
									"try" lineTo type(
										"number" lineTo type())))))))
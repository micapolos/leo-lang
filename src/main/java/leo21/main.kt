package leo21

import leo14.run
import leo21.token.body.emptyBodyCompiler
import leo21.token.processor.asTokenProcessor
import leo21.token.processor.stringCharReducer

fun main() {
	run(emptyBodyCompiler.asTokenProcessor.stringCharReducer)
}

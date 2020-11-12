package leo21

import leo14.run
import leo21.token.processor.emptyCompilerTokenProcessor
import leo21.token.processor.stringCharReducer

fun main() {
	run(emptyCompilerTokenProcessor.stringCharReducer)
}

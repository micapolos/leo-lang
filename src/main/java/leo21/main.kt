package leo21

import leo14.run
import leo21.token.body.emptyDefineCompiler
import leo21.token.processor.processor
import leo21.token.processor.stringCharReducer

fun main() {
	run(emptyDefineCompiler.processor.stringCharReducer)
}

package leo21.token.processor

import leo15.dsl.*
import leo21.token.processor.Processor
import leo21.token.processor.reducer

fun Processor.process(f: F): Processor =
	reducer.read(f).reduced

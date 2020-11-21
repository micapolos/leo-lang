package leo21.compiled

import leo15.dsl.*
import leo21.token.processor.compiled
import leo21.token.processor.emptyBodyProcessor
import leo21.token.processor.plus

fun compiled(f: F): Compiled =
	emptyBodyProcessor.plus(script_(f)).compiled
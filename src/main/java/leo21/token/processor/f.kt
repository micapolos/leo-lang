package leo21.token.processor

import leo15.dsl.*

fun processor(f: F) = emptyEvaluatorProcessor.plus(script_(f))
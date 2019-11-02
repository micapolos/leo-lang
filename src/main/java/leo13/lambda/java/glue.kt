package leo13.lambda.java

import leo13.lambda.Gen
import leo13.lambda.Variable

typealias Value = leo13.lambda.Value<Native>

fun Variable<Native>.index(gen: Gen) =
	gen.depth - index - 1

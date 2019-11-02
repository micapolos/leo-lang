package leo13.lambda.java

import leo13.lambda.*

typealias JavaValue = Value<Java>
typealias JavaAbstraction = Abstraction<JavaValue>
typealias JavaApplication = Application<JavaValue>
typealias JavaVariable = Variable<Java>

fun JavaVariable.index(gen: Gen) =
	gen.depth - index - 1

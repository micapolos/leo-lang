package leo16.lambda.type

import leo.base.parameter
import leo.base.runWith
import leo16.names.*

val repeatTypeParameter = parameter(type(_repeat()))

val repeatType: Type
	get() =
		repeatTypeParameter.value

fun <T> runRepeating(type: Type, fn: () -> T): T =
	repeatTypeParameter.runWith(type, fn)
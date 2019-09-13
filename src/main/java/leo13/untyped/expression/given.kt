package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.givenName

object Given

val given = Given
val Given.scriptLine get() = givenName lineTo script()
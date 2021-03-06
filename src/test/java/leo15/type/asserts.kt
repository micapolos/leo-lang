package leo15.type

import leo.base.assertEqualTo
import leo14.Script

val Script.assertTypeSerializes
	get() =
		type.script.assertEqualTo(this)

fun Typed.assertEvalsTo(typed: Typed) =
	eval.assertEqualTo(typed)

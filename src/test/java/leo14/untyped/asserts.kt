package leo14.untyped

import leo.base.assertEqualTo
import leo14.Script
import leo14.ScriptLine
import leo14.script

fun Script.assertEvalsTo(script: Script) =
	eval.assertEqualTo(script)

fun Script.assertEvalsTo(vararg lines: ScriptLine) =
	assertEvalsTo(script(*lines))

fun Script.assertEvalsGives(fn: Script.() -> Script) =
	eval.assertEqualTo(fn())

val Script.assertEvalsToThis
	get() =
		assertEvalsTo(this)

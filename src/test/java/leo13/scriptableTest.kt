package leo13

import leo.base.assertEqualTo

fun <V : Scriptable> V.scriptableAssert(code: String) =
	script(asScriptLine).assertEqualTo(unsafeScript(code))

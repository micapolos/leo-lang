package leo13.scripter

import leo13.fail
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine

fun <V : Any, V1 : Any, V2 : Any> scripter(
	name: String,
	case1: Case<V, V1>,
	case2: Case<V, V2>,
	scriptLineFn: V.() -> ScriptLine): Scripter<V> =
	Scripter(
		name,
		{ script(scriptLineFn(this)) },
		{
			unsafeOnlyLine
				.let { line ->
					when {
						case2.scripter.name == line.name -> case2.valueFn(case2.scripter.unsafeBodyValue(this))
						case1.scripter.name == line.name -> case1.valueFn(case1.scripter.unsafeBodyValue(this))
						else -> fail("mismatch" lineTo this)
					}
				}
		})

fun <V : Any, V1 : Any, V2 : Any, V3 : Any> scripter(
	name: String,
	case1: Case<V, V1>,
	case2: Case<V, V2>,
	case3: Case<V, V3>,
	scriptLineFn: V.() -> ScriptLine): Scripter<V> =
	Scripter(
		name,
		{ script(scriptLineFn(this)) },
		{
			unsafeOnlyLine
				.let { line ->
					when {
						case3.scripter.name == line.name -> case3.valueFn(case3.scripter.unsafeBodyValue(this))
						case2.scripter.name == line.name -> case2.valueFn(case2.scripter.unsafeBodyValue(this))
						case1.scripter.name == line.name -> case1.valueFn(case1.scripter.unsafeBodyValue(this))
						else -> fail("mismatch" lineTo this)
					}
				}
		})

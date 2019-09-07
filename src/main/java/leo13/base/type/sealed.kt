package leo13.base.type

import leo13.fail
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine

fun <V : Any, V1 : Any, V2 : Any> type(
	name: String,
	case1: Case<V, V1>,
	case2: Case<V, V2>,
	scriptLineFn: V.() -> ScriptLine): Type<V> =
	Type(
		name,
		{ script(scriptLineFn(this)) },
		{
			unsafeOnlyLine
				.let { line ->
					when {
						case2.type.name == line.name -> case2.valueFn(case2.type.unsafeBodyValue(this))
						case1.type.name == line.name -> case1.valueFn(case1.type.unsafeBodyValue(this))
						else -> fail("mismatch" lineTo this)
					}
				}
		})

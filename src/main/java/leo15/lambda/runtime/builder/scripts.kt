package leo15.lambda.runtime.builder

import leo14.*
import leo15.getName
import leo15.invokeName
import leo15.lambdaName
import leo15.valueName

typealias ScriptFn<T> = T.() -> Script

val anyScriptFn: ScriptFn<Any?> = { script(toString()) }

fun <V> Term<V>.script(scriptFn: ScriptFn<V>): Script =
	when (this) {
		is ValueTerm -> script(valueName(value.scriptFn()))
		is AbstractionTerm -> script(lambdaName(body.script(scriptFn)))
		is ApplicationTerm -> lhs.script(scriptFn).plus(invokeName(rhs.script(scriptFn)))
		is IndexTerm -> script(getName(script(literal(index))))
	}

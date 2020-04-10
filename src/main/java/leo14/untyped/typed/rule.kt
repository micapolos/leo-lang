package leo14.untyped.typed

import leo.base.notNullIf
import leo14.lambda.runtime.Value
import leo14.untyped.doesName

data class Rule(val fromType: Type, val toType: Type, val valueFn: (Value) -> Value)

fun rule(fromType: Type, toType: Type, fn: (Value) -> Value) = Rule(fromType, toType, fn)

fun Rule.apply(compiled: Compiled): Compiled? =
	notNullIf(fromType == compiled.type) {
		toType.compiled(
			compiled.expression.doApply {
				valueFn(this)
			})
	}

fun Compiled.ruleOrNull(scope: Scope): Rule? =
	type.matchInfix(doesName) { rhs ->
		this@ruleOrNull.linkOrNull?.let { link ->
			link.lhs.type.staticOrNull?.let { type ->
				link.line.rhsOrNull?.let { rhs ->
					scope.compiled(scope.script(rhs.evaluate.typed)).let { compiled ->
						rule(type, compiled.type) {
							TODO()
						}
					}
				}
			}
		}
	}

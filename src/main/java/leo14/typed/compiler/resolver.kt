package leo14.typed.compiler

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo13.stack
import leo14.lambda.Term
import leo14.lambda.native
import leo14.lambda.pair
import leo14.lambda.term
import leo14.typed.*

data class Resolver<T>(val ruleStack: Stack<Rule<T>>)
data class Rule<T>(val arrow: Arrow, val resolve: Term<T>.() -> Term<T>)

fun <T> resolver(vararg rules: Rule<T>) = Resolver(stack(*rules))
fun <T> rule(arrow: Arrow, resolve: Term<T>.() -> Term<T>) = Rule(arrow, resolve)

fun <T> Resolver<T>.resolve(typed: Typed<T>): Typed<T>? =
	ruleStack
		.mapFirst {
			notNullIf(arrow.lhs == typed.type) {
				typed.term.resolve() of arrow.rhs
			}
		}

val anyResolver: Resolver<Any> =
	resolver(
		rule(
			type(
				nativeLine,
				"plus" lineTo type(nativeLine)) arrowTo type(nativeLine)) {

			pair().run { term((first.native as Int) + (second.native as Int)) }
		}
	)
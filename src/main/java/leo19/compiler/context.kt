package leo19.compiler

import leo.base.fold
import leo.base.indexed
import leo.base.reverse
import leo13.onlyOrNull
import leo13.seq
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.ArrowType
import leo19.type.Choice
import leo19.type.ChoiceType
import leo19.type.IntRangeType
import leo19.type.StructType
import leo19.type.Type
import leo19.type.arrowTo
import leo19.type.field
import leo19.type.fieldTo
import leo19.type.isSimple
import leo19.type.type
import leo19.typed.Typed

data class Context(
	val resolver: Resolver,
	val scope: Scope
) {
	override fun toString() = reflect.toString()
}

val Context.reflect: ScriptLine
	get() =
		"context" lineTo script(
			resolver.reflect,
			scope.reflect)

fun Resolver.context(scope: Scope) = Context(this, scope)
val Resolver.emptyContext get() = context(scope())

val emptyContext = Context(emptyResolver, scope())

fun Context.defineChoice(type: Type): Context =
	defineChoice(type) { it }

fun Context.define(choice: Choice, wrapFn: (Type) -> Type): Context =
	choice.isSimple.let { isSimple ->
		fold(choice.caseStack.seq.reverse.indexed) { indexedCase ->
			Context(
				resolver.plus(
					functionBinding(
						Arrow(
							wrapFn(type(indexedCase.value.field)),
							wrapFn(ChoiceType(choice))))),
				scope.plus(
					if (isSimple) term(indexedCase.index)
					else term(term(indexedCase.index), term(variable(0)))))
		}
	}

fun Context.defineChoice(type: Type, wrapFn: (Type) -> Type): Context =
	when (type) {
		is StructType ->
			type.struct.fieldStack.onlyOrNull!!.let { field ->
				defineChoice(field.type) { innerType ->
					type(field.name fieldTo wrapFn(innerType))
				}
			}
		is ChoiceType -> define(type.choice, wrapFn)
		is ArrowType -> null
		is IntRangeType -> null
	}!!

fun Context.defineIs(type: Type, typed: Typed): Context =
	Context(
		resolver.plus(constantBinding(type arrowTo typed.type)),
		scope.plus(typed.term))

fun Context.defineGives(type: Type, typed: Typed): Context =
	Context(
		resolver.plus(functionBinding(type arrowTo typed.type)),
		scope.plus(typed.term))

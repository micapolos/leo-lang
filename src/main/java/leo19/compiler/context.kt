package leo19.compiler

import leo.base.fold
import leo.base.indexed
import leo.base.reverse
import leo13.Stack
import leo13.onlyOrNull
import leo13.push
import leo13.seq
import leo13.stack
import leo19.term.Term
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.ArrowType
import leo19.type.Choice
import leo19.type.ChoiceType
import leo19.type.StructType
import leo19.type.Type
import leo19.type.field
import leo19.type.fieldTo
import leo19.type.isSimple
import leo19.type.struct

data class Context(
	val termStack: Stack<Term>,
	val resolver: Resolver
)

val emptyContext = Context(stack(), emptyResolver)

fun Context.defineChoice(type: Type): Context =
	defineChoice(type) { it }

fun Context.define(choice: Choice, wrapFn: (Type) -> Type): Context =
	choice.isSimple.let { isSimple ->
		fold(choice.caseStack.seq.reverse.indexed) { indexedCase ->
			Context(
				termStack.push(
					if (isSimple) term(indexedCase.index)
					else term(term(indexedCase.index), term(variable(0)))),
				resolver.plus(
					binding(
						Arrow(
							wrapFn(struct(indexedCase.value.field)),
							wrapFn(ChoiceType(choice))))))
		}
	}

fun Context.defineChoice(type: Type, wrapFn: (Type) -> Type): Context =
	when (type) {
		is StructType ->
			type.struct.fieldStack.onlyOrNull!!.let { field ->
				defineChoice(field.type) { innerType ->
					struct(field.name fieldTo wrapFn(innerType))
				}
			}
		is ChoiceType -> define(type.choice, wrapFn)
		is ArrowType -> null
	}!!

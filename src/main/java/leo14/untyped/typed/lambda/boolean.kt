package leo14.untyped.typed.lambda

import leo.base.indexed
import leo14.lambda2.Term
import leo14.lambda2.nil
import leo14.lambda2.valueTerm
import leo14.untyped.booleanName
import leo14.untyped.falseName
import leo14.untyped.trueName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.or
import leo14.untyped.typed.type

val booleanType = type(booleanName lineTo type(trueName).or(type(falseName)))
val Boolean.alternativeIndex: Int get() = if (this) 1 else 0
val Boolean.value: Any? get() = alternativeIndex indexed nil
val Boolean.term: Term get() = value.valueTerm
val Boolean.typed: Typed get() = booleanType.typed(term)
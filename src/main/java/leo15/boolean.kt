package leo15

import leo.base.indexed
import leo14.lambda2.Term
import leo14.lambda2.nil
import leo14.lambda2.valueTerm
import leo14.untyped.booleanName
import leo14.untyped.falseName
import leo14.untyped.trueName

val booleanType = type(booleanName lineTo type(trueName).or(type(falseName)))
val Boolean.alternativeIndex: Int get() = if (this) 1 else 0
val Boolean.value: Any? get() = alternativeIndex indexed nil
val Boolean.term: Term get() = value.valueTerm
val Boolean.typed: Typed get() = booleanType.typed(term)
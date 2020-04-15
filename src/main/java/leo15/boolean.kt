package leo15

import leo.base.indexed
import leo15.lambda.Term
import leo15.lambda.nilTerm
import leo15.lambda.valueTerm

val booleanType = type(booleanName lineTo type(trueName).or(type(falseName)))
val Boolean.alternativeIndex: Int get() = if (this) 1 else 0
val Boolean.value: Any? get() = alternativeIndex indexed nilTerm
val Boolean.term: Term get() = value.valueTerm
val Boolean.typed: Typed get() = booleanType.typed(term)
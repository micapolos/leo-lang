package leo14.type.typed

import leo14.lambda.Term
import leo14.type.Thunk

typealias TypeThunk = Thunk

data class Typed<T>(val term: Term<T>, val typeThunk: TypeThunk)
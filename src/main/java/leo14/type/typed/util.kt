package leo14.type.typed

import leo14.lambda.Term

infix fun <T> Term<T>.of(typeThunk: TypeThunk) = Typed(this, typeThunk)


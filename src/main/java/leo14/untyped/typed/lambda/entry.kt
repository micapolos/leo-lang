package leo14.untyped.typed.lambda

import leo14.lambda2.Term
import java.lang.reflect.Type

data class Entry(val type: Type, val term: Term, val isConstant: Boolean)

fun Type.constantEntry(term: Term) = Entry(this, term, true)
fun Type.functionEntry(term: Term) = Entry(this, term, false)

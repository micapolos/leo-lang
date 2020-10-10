package leo19.term

sealed class Term
data class IntTerm(val int: Int) : Term()
data class TupleTerm(val list: List<Term>) : Term()
data class TupleGetTerm(val tuple: Term, val index: Term) : Term()
data class FunctionTerm(val body: Term) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class VariableTerm(val index: Int) : Term()

fun term(int: Int): Term = IntTerm(int)
fun term(vararg terms: Term): Term = TupleTerm(terms.toList())
fun Term.get(index: Term): Term = TupleGetTerm(this, index)
fun functionTerm(term: Term): Term = FunctionTerm(term)
fun Term.invoke(term: Term): Term = InvokeTerm(this, term)
fun variableTerm(index: Int): Term = VariableTerm(index)
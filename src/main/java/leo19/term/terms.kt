package leo19.term

val Boolean.term: Term get() = term(if (this) 0 else 1)
val Term.boolean: Boolean get() = (this as IntTerm).int == 0
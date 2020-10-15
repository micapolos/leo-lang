package leo19.term

val Boolean.term: Term get() = term(if (this) 0 else 1)

package leo.term

typealias Pattern = Term<Matcher>

fun pattern(term: Term<Matcher>): Pattern = term

// === pattern matching script

fun Matcher.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	when (this) {
		is OneOfMatcher -> oneOf.isMatching(term, traceOrNull)
		is RecursionMatcher -> recursion.isMatching(term, traceOrNull?.back)
	}

fun OneOf.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	patternStack.head.isMatching(term, traceOrNull)
		|| patternStack.tail?.oneOf?.isMatching(term, traceOrNull) ?: false

fun Recursion.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	apply(traceOrNull)?.let { trace ->
		trace.lastPattern.isMatching(term, trace.back)
	} ?: false

fun Term<Matcher>?.orNullIsInstance(termOrNull: Term<Nothing>?, traceOrNull: Trace? = null): Boolean =
	if (this == null) termOrNull == null
	else termOrNull != null && isMatching(termOrNull, traceOrNull)

fun Term<Matcher>.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	when (this) {
		is ValueTerm -> this.isMatching(term, traceOrNull)
		is ApplicationTerm -> term is ApplicationTerm && this.isMatching(term, traceOrNull)
	}

fun ValueTerm<Matcher>.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	value.isMatching(term, traceOrNull.plus(this))

fun ApplicationTerm<Matcher>.isMatching(term: ApplicationTerm<Nothing>, traceOrNull: Trace? = null): Boolean =
	subject.isMatching(term.subject, traceOrNull.plus(this))
		&& application.isMatching(term.application, traceOrNull.plus(this))

fun Application<Matcher>.isMatching(application: Application<Nothing>, traceOrNull: Trace? = null): Boolean =
	word == application.word && parameter.isMatching(application.parameter, traceOrNull)

fun Subject<Matcher>.isMatching(subject: Subject<Nothing>, traceOrNull: Trace? = null): Boolean =
	termOrNull.orNullIsInstance(subject.termOrNull, traceOrNull)

fun Parameter<Matcher>.isMatching(parameter: Parameter<Nothing>, traceOrNull: Trace? = null): Boolean =
	termOrNull.orNullIsInstance(parameter.termOrNull, traceOrNull)

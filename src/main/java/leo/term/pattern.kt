package leo.term

typealias Pattern = Term<Expander>

fun pattern(term: Term<Expander>): Pattern = term

fun Expander.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	when (this) {
		is OneOfExpander -> oneOf.isMatching(term, traceOrNull)
		is RecursionExpander -> recursion.isMatching(term, traceOrNull?.back)
	}

fun OneOf.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	patternStack.head.isMatching(term, traceOrNull)
		|| patternStack.tail?.oneOf?.isMatching(term, traceOrNull) ?: false

fun Recursion.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	apply(traceOrNull)?.let { trace ->
		trace.lastPattern.isMatching(term, trace.back)
	} ?: false

fun Term<Expander>?.orNullIsInstance(termOrNull: Term<Nothing>?, traceOrNull: Trace? = null): Boolean =
	if (this == null) termOrNull == null
	else termOrNull != null && isMatching(termOrNull, traceOrNull)

fun Term<Expander>.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	when (this) {
		is ValueTerm -> this.isMatching(term, traceOrNull)
		is ApplicationTerm -> term is ApplicationTerm && this.isMatching(term, traceOrNull)
	}

fun ValueTerm<Expander>.isMatching(term: Term<Nothing>, traceOrNull: Trace? = null): Boolean =
	value.isMatching(term, traceOrNull.plus(this))

fun ApplicationTerm<Expander>.isMatching(term: ApplicationTerm<Nothing>, traceOrNull: Trace? = null): Boolean =
	receiver.isMatching(term.receiver, traceOrNull.plus(this))
		&& application.isMatching(term.application, traceOrNull.plus(this))

fun Application<Expander>.isMatching(application: Application<Nothing>, traceOrNull: Trace? = null): Boolean =
	word == application.word && argument.isMatching(application.argument, traceOrNull)

fun Receiver<Expander>.isMatching(receiver: Receiver<Nothing>, traceOrNull: Trace? = null): Boolean =
	termOrNull.orNullIsInstance(receiver.termOrNull, traceOrNull)

fun Argument<Expander>.isMatching(argument: Argument<Nothing>, traceOrNull: Trace? = null): Boolean =
	termOrNull.orNullIsInstance(argument.termOrNull, traceOrNull)

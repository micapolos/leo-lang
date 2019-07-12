package lambda.indexed

import leo.base.*
import leo.binary.zero

object Unquote

val unquote = Unquote

val Term.isId
	get() =
		functionOrNull?.body?.term?.argumentOrNull?.nat?.zeroOrNull != null

val Term.zeroOrNull: Nat?
	get() =
		notNullIf(isId) { zero.nat }

val Term.natOrNull: Nat?
	get() =
		functionTermOrNull(2)
			?.applicationOrNull
			?.let { application ->
				application.lhs.argumentOrNull?.nat?.switch(
					{ application.rhs.zeroOrNull },
					{ application.rhs.natOrNull?.inc })
			}

val Term.unquoteBody: Body?
	get() =
		unquoteTerm?.let(::body)


val Term.unquoteArgument: Argument?
	get() =
		natOrNull?.let(::argument)

val Term.unquoteApplication: Application?
	get() =
		functionTermOrNull(1)
			?.applicationOrNull
			?.let { application ->
				application.rhs.unquoteTerm?.let { unquotedRhs ->
					application.lhs.applicationOrNull?.let { app1 ->
						app1.rhs.unquoteTerm?.let { unquotedLhs ->
							app1.lhs.argumentOrNull?.nat?.switch(
								{ application(unquotedLhs, unquotedRhs) })
						}
					}
				}
			}

val Term.unquoteFunction: Function?
	get() =
		unquoteBody?.let(::function)

val Term.unquoteQuote: Quote?
	get() =
		notNullIf(isId) { quote }

val Term.unquoteUnquote: Unquote?
	get() =
		notNullIf(isId) { unquote }

val Term.unquoteTerm: Term?
	get() =
		functionTermOrNull(5)
			?.applicationOrNull
			?.let { application ->
				application.lhs.argumentOrNull
					?.nat
					?.switch(
						{ application.rhs.unquoteArgument?.let(::term) },
						{ application.rhs.unquoteApplication?.let(::term) },
						{ application.rhs.unquoteFunction?.let(::term) },
						{ application.rhs.unquoteQuote?.let(::term) },
						{ application.rhs.unquoteUnquote?.let(::term) })
			}

package leo16

import leo13.Either
import leo13.Empty
import leo13.FirstEither
import leo13.Link
import leo13.SecondEither
import leo13.Stack
import leo13.empty
import leo13.firstEither
import leo13.linkTo
import leo13.push
import leo13.reverse
import leo13.secondEither
import leo13.stack
import leo16.names.*

tailrec fun Stack<Value>.pushOrNull(field: Sentence): Stack<Value>? {
	val either = field.parseEitherEmptyOrLink ?: return null
	return when (either) {
		is FirstEither -> this
		is SecondEither -> {
			val head = either.second.head
			val tail = either.second.tail.onlySentenceOrNull ?: return null
			push(head).pushOrNull(tail)
		}
	}
}

val Sentence.parseEitherEmptyOrLink: Either<Empty, Link<Value, Value>>?
	get() =
		matchPrefix(_list) { rhs ->
			null
				?: rhs.onlySentenceOrNull?.parseEmpty?.firstEither()
				?: rhs.onlySentenceOrNull?.parseLink?.secondEither()
		}

val Sentence.parseEmpty: Empty?
	get() =
		matchPrefix(_empty) { rhs ->
			rhs.matchEmpty {
				empty
			}
		}

val Sentence.parseLink: Link<Value, Value>?
	get() =
		matchPrefix(_link) { rhs ->
			rhs.matchInfix(_last) { lhs, last ->
				lhs.matchPrefix(_previous) { previous ->
					previous.linkTo(last)
				}
			}
		}

val Sentence.stackOrNull: Stack<Value>?
	get() =
		stack<Value>().pushOrNull(this)?.reverse

val Value.stackOrNull: Stack<Value>?
	get() =
		onlySentenceOrNull?.stackOrNull


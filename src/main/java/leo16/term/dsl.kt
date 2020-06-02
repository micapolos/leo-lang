package leo16.term

fun <T> T.value(): Term<T> = valueTerm
fun <T> at(index: Int) = index.variableTerm<T>()
fun <T> lambda(term: Term<T>) = term.abstractionTerm
operator fun <T> Term<T>.invoke(term: Term<T>) = applicationTerm(term)

fun <T> id() = lambda(at<T>(0))
fun <T> pairFirst() = lambda(lambda(at<T>(0)))
fun <T> pairSecond() = lambda(lambda(at<T>(1)))
fun <T> pair() = lambda(lambda(lambda(at<T>(0).invoke(at(2)).invoke(at(1)))))
fun <T> eitherFirst() = lambda(lambda(lambda(at<T>(1).invoke(at(2)))))
fun <T> eitherSecond() = lambda(lambda(lambda(at<T>(0).invoke(at(2)))))

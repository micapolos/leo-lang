package leo15.lambda.builder

fun <T> id() = lambda<T>(get(0))
fun <T> const() = lambda<T>(lambda(get(1)))
fun <T> first() = lambda<T>(lambda(get(1)))
fun <T> second() = lambda<T>(lambda(get(0)))
fun <T> pair() = lambda(lambda(lambda(get<T>(0)(get(2))(get(1)))))

fun <T> firstEither() = lambda(lambda(lambda(get<T>(1).invoke(get(2)))))
fun <T> secondEither() = lambda(lambda(lambda(get<T>(0).invoke(get(2)))))

fun <T> Term<T>.append(rhs: Term<T>) = pair<T>().invoke(this).invoke(rhs)
define(
	it(leo(read(token(begin(foo())))))
	is(leo(read(token(begin(zoo()))))))

define(
	it(leo(read(token(end()))))
	is(
		leo(read(token(end())))
		leo(read(token(begin(and()))))
		leo(read(token(end())))))

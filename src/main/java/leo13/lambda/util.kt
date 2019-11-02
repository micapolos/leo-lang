package leo13.lambda

fun <T> arrowExpr(lhs: Expr<T>, rhs: Expr<T>): Expr<T> =
	expr(
		ap(
			expr(
				ap(
					expr(
						fn(
							expr(
								fn(
									expr(
										fn(
											expr(
												fn(
													expr(
														fn(
															expr(
																arg(0)))))))))))),
					lhs)),
			rhs))

fun <T> lhsExpr(expr: Expr<T>): Expr<T> = TODO()

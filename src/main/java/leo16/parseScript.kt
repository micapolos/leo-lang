package leo16

import leo.base.int
import leo.base.short
import leo13.base.Bit
import leo13.base.byte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.bitName
import leo14.Literal
import leo14.literal
import leo15.*

val Script.bitOrNull: Bit?
	get() =
		matchPrefix(bitName) { rhs ->
			rhs.matchWord { word ->
				when (word) {
					zeroName -> zeroBit
					oneName -> oneBit
					else -> null
				}
			}
		}

val Script.byteOrNull: Byte?
	get() =
		matchPrefix(byteName) { rhs ->
			rhs.matchInfix(eighthName) { lhs, rhs ->
				rhs.bitOrNull?.let { eighth ->
					lhs.matchInfix(seventhName) { lhs, rhs ->
						rhs.bitOrNull?.let { seventh ->
							lhs.matchInfix(sixthName) { lhs, rhs ->
								rhs.bitOrNull?.let { sixth ->
									lhs.matchInfix(fifthName) { lhs, rhs ->
										rhs.bitOrNull?.let { fifth ->
											lhs.matchInfix(fourthName) { lhs, rhs ->
												rhs.bitOrNull?.let { fourth ->
													lhs.matchInfix(thirdName) { lhs, rhs ->
														rhs.bitOrNull?.let { third ->
															lhs.matchInfix(secondName) { lhs, rhs ->
																rhs.bitOrNull?.let { second ->
																	lhs.matchPrefix(firstName) { rhs ->
																		rhs.bitOrNull?.let { first ->
																			byte(first, second, third, fourth, fifth, sixth, seventh, eighth)
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Script.intOrNull: Int?
	get() =
		matchPrefix(intName) { rhs ->
			rhs.matchInfix(fourthName) { lhs, rhs ->
				rhs.byteOrNull?.let { fourth ->
					lhs.matchInfix(thirdName) { lhs, rhs ->
						rhs.byteOrNull?.let { third ->
							lhs.matchInfix(secondName) { lhs, rhs ->
								rhs.byteOrNull?.let { second ->
									lhs.matchPrefix(firstName) { rhs ->
										rhs.byteOrNull?.let { first ->
											int(short(first, second), short(third, fourth))
										}
									}
								}
							}
						}
					}
				}
			}
		}

val Script.stringOrNull: String?
	get() =
		null // TODO()

val Script.literalOrNull: Literal?
	get() =
		null
			?: intOrNull?.literal
			?: stringOrNull?.literal
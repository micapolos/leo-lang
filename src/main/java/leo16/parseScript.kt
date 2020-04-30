package leo16

import leo.base.int
import leo.base.short
import leo.base.utf8String
import leo13.*
import leo13.base.Bit
import leo13.base.byte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.bitName
import leo14.Literal
import leo14.literal
import leo15.*
import leo15.byteName
import leo15.linkName
import leo15.listName
import leo15.oneName
import leo15.previousName
import leo15.zeroName

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

// TODO: Convert to tailrec
fun <T> Script.stackOrNull(fn: Script.() -> T?): Stack<T>? =
	matchPrefix(listName) { rhs ->
		rhs.matchLink { lhs, word, rhs ->
			when (word) {
				nothingName ->
					lhs.matchEmpty {
						rhs.matchEmpty {
							stack<T>()
						}
					}
				linkName ->
					lhs.matchEmpty {
						rhs.matchInfix(lastName) { lhs, rhs ->
							rhs.fn()?.let { value ->
								lhs.matchPrefix(previousName) { rhs ->
									rhs.stackOrNull(fn)?.let { stack ->
										stack.push(value)
									}
								}
							}
						}
					}
				else -> null
			}
		}
	}

val Script.stringOrNull: String?
	get() =
		matchPrefix(stringName) { rhs ->
			rhs
				.stackOrNull { byteOrNull }
				?.array
				?.toByteArray()
				?.utf8String
		}

val Sentence.literalOrNull: Literal?
	get() =
		script(this).literalOrNull

val Script.literalOrNull: Literal?
	get() =
		null
			?: intOrNull?.literal
			?: stringOrNull?.literal
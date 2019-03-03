package leo

import leo.base.bitStream
import leo.base.byte
import leo.base.ifNull
import leo.base.matchFirst
import leo.binary.Bit

val Byte.reflect: Field<Nothing>
	get() =
		byteWord fieldTo bitStream.reflect(Bit::reflect)

val Field<Nothing>.parseByte: Byte?
	get() =
		matchKey(byteWord) {
			structureTermOrNull?.fieldStream?.run {
				matchFirst { field7 ->
					field7.parseBit?.let { bit7 ->
						this?.matchFirst { field6 ->
							field6.parseBit?.let { bit6 ->
								this?.matchFirst { field5 ->
									field5.parseBit?.let { bit5 ->
										this?.matchFirst { field4 ->
											field4.parseBit?.let { bit4 ->
												this?.matchFirst { field3 ->
													field3.parseBit?.let { bit3 ->
														this?.matchFirst { field2 ->
															field2.parseBit?.let { bit2 ->
																this?.matchFirst { field1 ->
																	field1.parseBit?.let { bit1 ->
																		this?.matchFirst { field0 ->
																			field0.parseBit?.let { bit0 ->
																				ifNull {
																					byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
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
			}
		}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.leoniedermeier.utils.excecption;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A runtime exception that provides an easy and safe way to add contextual
 * information.
 * </p>
 * <p>
 * An exception trace itself is often insufficient to provide rapid diagnosis of
 * the issue. Frequently what is needed is a select few pieces of local
 * contextual data. Providing this data is tricky however, due to concerns over
 * formatting and nulls.
 * </p>
 * <p>
 * The contexted exception approach allows the exception to be created together
 * with a list of context label-value pairs. This additional information is
 * automatically included in the message and printed stack trace.
 * </p>
 * <p>
 * To use this class write code as follows:
 * </p>
 * 
 * <pre>
 *   try {
 *     ...
 *   } catch (Exception e) {
 *     throw new ContextedRuntimeException(SomeErrorId, e)
 *          .addContextValue("Account Number", accountNumber)
 *          .addContextValue("Amount Posted", amountPosted)
 *          .addContextValue("Previous Balance", previousBalance)
 *   }
 * }
 * </pre>
 * <p>
 * or improve diagnose data at a higher level:
 * </p>
 * 
 * <pre>
 *   try {
 *     ...
 *   } catch (ContextedRuntimeException e) {
 *     throw e.setContextValue("Transaction Id", transactionId);
 *   }
 * }
 * </pre>
 * <p>
 * The output in a printStacktrace() (which often is written to a log) would
 * look something like the following:
 * </p>
 * 
 * <pre>
 * io.github.leoniedermeier.utils.excecption.ContextedRuntimeException: 
 *  ErrorCodes=CODE_1, CODE_2
 *  CID=C7BWD6
 *  ---------------------------------
 *  Exception Context:
 *    [1:ErrorCode=CODE_1]
 *    [2:CID=C7BWD6]
 *    [3:label=MyValue]
 *    [4:ErrorCode=CODE_2]
 *  ---------------------------------
 *  ..... (rest of trace)
 * </pre>
 *
 */

public class ContextedRuntimeException extends RuntimeException
		implements ExtendedExceptionContext<ContextedRuntimeException> {

	private static final long serialVersionUID = 6933021659100487064L;

	/** Where the data is stored. */
	private final List<Entry> entries = new ArrayList<>();

	public ContextedRuntimeException(ErrorCode errorCode) {
		super();
		init(errorCode);
	}

	public ContextedRuntimeException(ErrorCode errorCode, final String message) {
		super(message);
		init(errorCode);
	}

	public ContextedRuntimeException(ErrorCode errorCode, final String message, final Throwable cause) {
		super(message, cause);
		init(errorCode);
	}

	public ContextedRuntimeException(ErrorCode errorCode, final Throwable cause) {
		super(cause);
		init(errorCode);
	}

	@Override
	public List<Entry> getContextEntries() {
		return this.entries;
	}

	/**
	 * Provides the message explaining the exception, including the contextual data.
	 *
	 * @see java.lang.Throwable#getMessage()
	 * @return the message, never {@code null}.
	 */
	@Override
	public String getMessage() {
		return getFormattedExceptionMessage(super.getMessage());
	}

	/**
	 * Provides the message explaining the exception without the contextual data.
	 *
	 * @see java.lang.Throwable#getMessage()
	 * @return the message
	 */
	public String getRawMessage() {
		return super.getMessage();
	}
}

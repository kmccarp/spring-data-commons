/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.domain;

/**
 * Interface defining a request for a cursor window. Typically used to fetch subsets of query results if the query
 * results are too large to fit entirely into memory and to be able to resume a cursoring session.
 *
 * @author Mark Paluch
 * @since 3.1
 * @see OffsetCursorRequest
 * @see KeysetCursorRequest
 * @see CursorWindow
 * @see Pageable#toCursorRequest()
 */
public interface CursorRequest {

	/**
	 * Returns the number of items to be returned.
	 *
	 * @return the number of items of that cursor request.
	 */
	int getSize();

	/**
	 * Creates a new cursor request with the given size.
	 *
	 * @param size the cursor result size.
	 * @return a new {@link CursorRequest} with {@code size} applied.
	 */
	CursorRequest withSize(int size);

	/**
	 * Returns the sorting parameters for this cursor request.
	 *
	 * @return
	 */
	Sort getSort();

	/**
	 * Returns whether the current cursor window is the first one.
	 *
	 * @return
	 */
	boolean isFirst();

	/**
	 * Returns whether the current cursor window is the last one.
	 *
	 * @return
	 */
	// TODO: This method might go away since we only know if the request is a last one when we have attempted the cursor
	// request.
	default boolean isLast() {
		return !hasNext();
	}

	/**
	 * Returns if there is a next cursor window.
	 *
	 * @return if there is a next cursor window.
	 */
	boolean hasNext();

	/**
	 * Returns the next {@link CursorRequest}.
	 *
	 * @return a new cursor request to be used as the {@link #hasNext() next} cursor request.
	 * @throws IllegalStateException if there is no {@link #hasNext() next} cursor window.
	 */
	CursorRequest nextCursorRequest();

	/**
	 * Creates a {@link #isLast() last cursor request} if {@code isLast} is {@code true}.
	 *
	 * @param isLast whether the created cursor request should indicate to be the last cursor request.
	 * @return a new cursor request to be used as the {@link #isLast() last} cursor request.
	 */
	CursorRequest withLast(boolean isLast);
}

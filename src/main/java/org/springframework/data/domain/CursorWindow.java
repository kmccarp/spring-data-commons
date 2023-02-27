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

import java.util.List;

import org.springframework.data.util.Streamable;

/**
 * A window of data consumed from an underlying cursor. A cursor window is similar to {@link Slice} in the sense that it
 * contains a subset of the actual query results for easier consumption of large result sets. The cursor window is less
 * opinionated about the actual data retrieval, whether the query has used index/offset, keyset-based pagination or
 * cursor resume tokens.
 *
 * @author Mark Paluch
 * @since 3.1
 * @see CursorRequest
 */
public interface CursorWindow<T> extends Streamable<T> {

	/**
	 * Construct a {@link CursorWindow}.
	 *
	 * @param request the current cursor request that has led to this result.
	 * @param cursorWindow the window of data.
	 * @return the {@link CursorWindow}.
	 * @param <T>
	 */
	static <T> CursorWindow<T> from(CursorRequest request, List<T> cursorWindow) {
		return new CursorWindowImpl<>(request, cursorWindow);
	}

	/**
	 * Returns the number of elements in this cursor window.
	 *
	 * @return the number of elements in this cursor window.
	 */
	int size();

	/**
	 * Returns {@code true} if this cursor window contains no elements.
	 *
	 * @return {@code true} if this cursor window contains no elements
	 */
	boolean isEmpty();

	/**
	 * Returns the cursor window content as {@link List}.
	 *
	 * @return
	 */
	List<T> getContent();

	/**
	 * Returns the sorting parameters for the {@link Slice}.
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
	 * @return the {@link CursorRequest} to obtain the next cursor window.
	 * @throws IllegalStateException if there is no {@link #hasNext() next} cursor window.
	 */
	CursorRequest nextCursorRequest();

}

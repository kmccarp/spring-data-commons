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
 * @author Mark Paluch
 */
public interface CursorRequest {

	/**
	 * Returns the number of items to be returned.
	 *
	 * @return the number of items of that cursor request.
	 */
	int getSize();

	/**
	 * Create a new cursor request with the given size.
	 *
	 * @param size
	 * @return
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
	 * @return
	 * @throws IllegalStateException if there is no {@link #hasNext() next} cursor window.
	 */
	CursorRequest nextCursorRequest();

}

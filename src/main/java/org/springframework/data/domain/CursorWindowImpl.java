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

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Default {@link CursorWindow} implementation.
 *
 * @author Mark Paluch
 * @since 3.1
 */
class CursorWindowImpl<T> implements CursorWindow<T> {

	private final CursorRequest cursorRequest;

	private final List<T> items;

	CursorWindowImpl(CursorRequest cursorRequest, List<T> items) {

		Assert.notNull(cursorRequest, "CursorRequest must not be null");
		Assert.notNull(items, "List of items must not be null");

		this.cursorRequest = cursorRequest;
		this.items = items;
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public List<T> getContent() {
		return items;
	}

	@Override
	public Sort getSort() {
		return cursorRequest.getSort();
	}

	@Override
	public boolean isFirst() {
		return cursorRequest.isFirst();
	}

	@Override
	public boolean hasNext() {
		return cursorRequest.hasNext();
	}

	@Override
	public CursorRequest nextCursorRequest() {
		return cursorRequest.nextCursorRequest();
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CursorWindowImpl<?> that = (CursorWindowImpl<?>) o;
		return ObjectUtils.nullSafeEquals(items, that.items)
				&& ObjectUtils.nullSafeEquals(cursorRequest, that.cursorRequest);
	}

	@Override
	public int hashCode() {
		int result = ObjectUtils.nullSafeHashCode(items);
		result = 31 * result + ObjectUtils.nullSafeHashCode(cursorRequest);
		return result;
	}

	@Override
	public String toString() {
		return "CursorWindow (" + cursorRequest + ") " + items;
	}
}

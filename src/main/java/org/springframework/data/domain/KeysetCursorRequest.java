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

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * A {@link CursorRequest} based on keyset sorting. Keyset pagination is an approach to sort results and resume cursor
 * consumption after the last seen result that has been consumed.
 *
 * @author Mark Paluch
 * @since 3.1
 */
public class KeysetCursorRequest implements CursorRequest, Serializable {

	private final int size;

	private final Sort sort;

	private final Map<String, Object> keys;

	private final KeysetCursorRequest next;

	KeysetCursorRequest(int size, Sort sort, Map<String, Object> keys) {
		this(size, sort, keys, null);
	}

	KeysetCursorRequest(int size, Sort sort, Map<String, Object> keys, @Nullable KeysetCursorRequest next) {

		Assert.isTrue(size > 0, "Size must be greater than zero");

		this.size = size;
		this.sort = sort;
		this.keys = Collections.unmodifiableMap(keys);
		this.next = next;
	}

	public static KeysetCursorRequest ofSize(int pageSize, Sort sort) {

		Assert.isTrue(sort.isSorted(), "KeySet pagination requires a stable sort order");

		return new KeysetCursorRequest(pageSize, sort, Collections.emptyMap(), null);
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public KeysetCursorRequest withSize(int size) {
		return new KeysetCursorRequest(size, sort, keys, next);
	}

	public KeysetCursorRequest withNext(Map<String, Object> keys) {
		return new KeysetCursorRequest(size, sort, this.keys, new KeysetCursorRequest(size, sort, keys));
	}

	@Override
	public boolean isFirst() {
		return next == null;
	}

	@Override
	public CursorRequest nextCursorRequest() {

		Assert.state(!isLast(), "Cannot create a next cursor request beyond the end of the cursor");
		Assert.state(this.next != null, "Cannot create a next cursor from a non-executed KeysetCursorRequest");

		return next;
	}

	@Override
	public Sort getSort() {
		return this.sort;
	}

	public Map<String, Object> getKeys() {
		return this.keys;
	}

	public boolean hasNext() {
		return next != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		KeysetCursorRequest that = (KeysetCursorRequest) o;
		return size == that.size && ObjectUtils.nullSafeEquals(sort, that.sort)
				&& ObjectUtils.nullSafeEquals(keys, that.keys) && ObjectUtils.nullSafeEquals(next, that.next);
	}

	@Override
	public int hashCode() {

		int result = ObjectUtils.nullSafeHashCode(size);
		result = 31 * result + ObjectUtils.nullSafeHashCode(sort);
		result = 31 * result + ObjectUtils.nullSafeHashCode(keys);
		result = 31 * result + ObjectUtils.nullSafeHashCode(next);
		return result;
	}
}

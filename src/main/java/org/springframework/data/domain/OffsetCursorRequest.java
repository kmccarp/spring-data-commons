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

import org.springframework.util.Assert;

/**
 * Index/offset-based {@link CursorRequest}.
 *
 * @author Mark Paluch
 * @since 3.1
 */
public class OffsetCursorRequest implements CursorRequest, Serializable {

	private final int size;

	private final long offset;

	private final Sort sort;

	private final boolean isLast;

	OffsetCursorRequest(int size, long offset, Sort sort, boolean isLast) {

		Assert.notNull(sort, "Sort must not be null");
		Assert.isTrue(size > 0, "Size must be greater zero");
		Assert.isTrue(offset >= 0, "Offset must not be negative");

		this.size = size;
		this.offset = offset;
		this.sort = sort;
		this.isLast = isLast;
	}

	/**
	 * Create a new {@link OffsetCursorRequest} for a given {@code pageSize} and {@link Sort} starting at
	 * {@link #getOffset()} zero.
	 *
	 * @param pageSize
	 * @param sort
	 * @return a new {@link OffsetCursorRequest}.
	 */
	public static OffsetCursorRequest ofSize(int pageSize, Sort sort) {
		return new OffsetCursorRequest(pageSize, 0, sort, false);
	}

	@Override
	public int getSize() {
		return size;
	}

	public long getOffset() {
		return offset;
	}

	/**
	 * Creates a new {@link OffsetCursorRequest} with the given {@code size}.
	 *
	 * @param size
	 * @return
	 */
	@Override
	public OffsetCursorRequest withSize(int size) {
		return new OffsetCursorRequest(size, offset, sort, isLast);
	}

	/**
	 * Creates a new {@link OffsetCursorRequest} with the given {@code offset}. The new {@link OffsetCursorRequest} resets
	 * the {@link #isLast()} flag if the new offset doesn't match the current offset as you need to execute the new cursor
	 * request to determine whether there is more data to fetch.
	 *
	 * @param offset
	 * @return
	 */
	public OffsetCursorRequest withOffset(long offset) {
		return new OffsetCursorRequest(size, offset, sort, isLast && this.offset == offset);
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public boolean isFirst() {
		return offset == 0;
	}

	/**
	 * Create a terminal {@link OffsetCursorRequest} setting the {@link #isLast()} flag to {@code true}.
	 *
	 * @return
	 */
	public OffsetCursorRequest withLast() {
		return new OffsetCursorRequest(size, offset, sort, true);
	}

	@Override
	public boolean hasNext() {
		return !isLast;
	}

	/**
	 * Create a next {@link OffsetCursorRequest} fetching {@link #getSize()} elements and incrementing the offset by
	 * {@link #getSize()} accordingly.
	 *
	 * @return
	 */
	@Override
	public CursorRequest nextCursorRequest() {
		return new OffsetCursorRequest(size, offset + size, sort, isLast);
	}
}

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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OffsetCursorRequest}.
 *
 * @author Mark Paluch
 */
class OffsetCursorRequestUnitTests {

	@Test // GH-2151
	void equalsAndHashCode() {

		OffsetCursorRequest foo1 = OffsetCursorRequest.ofSize(1, Sort.by("foo"));
		OffsetCursorRequest foo2 = OffsetCursorRequest.ofSize(1, Sort.by("foo"));
		OffsetCursorRequest bar = OffsetCursorRequest.ofSize(1, Sort.by("bar"));

		assertThat(foo1).isEqualTo(foo2).hasSameClassAs(foo2);
		assertThat(foo1.withSize(3)).isNotEqualTo(foo2).doesNotHaveSameHashCodeAs(foo2);
		assertThat(foo1).isNotEqualTo(bar).doesNotHaveSameHashCodeAs(bar);

		OffsetCursorRequest foo1Next = foo1.withOffset(25);
		OffsetCursorRequest foo2Next = foo2.withOffset(25);
		OffsetCursorRequest foo2NextOtherVal = foo2.withOffset(26);

		assertThat(foo1Next).isEqualTo(foo2Next).hasSameClassAs(foo2Next);
		assertThat(foo1Next).isNotEqualTo(foo2NextOtherVal).doesNotHaveSameHashCodeAs(foo2NextOtherVal);
	}

	@Test // GH-2151
	void shouldHaveNext() {

		OffsetCursorRequest first = OffsetCursorRequest.ofSize(1, Sort.by("foo"));

		assertThat(first.isFirst()).isTrue();
		assertThat(first.hasNext()).isTrue();
		assertThat(first.isLast()).isFalse();

		OffsetCursorRequest next = first.nextCursorRequest();

		assertThat(next.isFirst()).isFalse();
		assertThat(next.hasNext()).isTrue();
		assertThat(next.isLast()).isFalse();
	}

	@Test // GH-2151
	void withLastShouldResetCursor() {

		OffsetCursorRequest last = OffsetCursorRequest.ofSize(1, Sort.by("foo")).withLast(true);
		OffsetCursorRequest reset = last.withOffset(1);

		assertThat(last.hasNext()).isFalse();
		assertThat(last.isLast()).isTrue();

		assertThat(reset.hasNext()).isTrue();
		assertThat(reset.isLast()).isFalse();
	}
}

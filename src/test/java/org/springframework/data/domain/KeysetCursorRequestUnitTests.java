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

import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link KeysetCursorRequest}.
 *
 * @author Mark Paluch
 */
class KeysetCursorRequestUnitTests {

	@Test // GH-2151
	void equalsAndHashCode() {

		KeysetCursorRequest foo1 = KeysetCursorRequest.ofSize(1, Sort.by("foo"));
		KeysetCursorRequest foo2 = KeysetCursorRequest.ofSize(1, Sort.by("foo"));
		KeysetCursorRequest bar = KeysetCursorRequest.ofSize(1, Sort.by("bar"));

		assertThat(foo1).isEqualTo(foo2).hasSameClassAs(foo2);
		assertThat(foo1.withSize(3)).isNotEqualTo(foo2).doesNotHaveSameHashCodeAs(foo2);
		assertThat(foo1).isNotEqualTo(bar).doesNotHaveSameHashCodeAs(bar);

		KeysetCursorRequest foo1Next = foo1.withNext(Collections.singletonMap("k", "v"));
		KeysetCursorRequest foo2Next = foo2.withNext(Collections.singletonMap("k", "v"));
		KeysetCursorRequest foo2NextOtherVal = foo2.withNext(Collections.singletonMap("k", "different"));

		assertThat(foo1Next).isEqualTo(foo2Next).hasSameClassAs(foo2Next);
		assertThat(foo1Next).isNotEqualTo(foo2NextOtherVal).doesNotHaveSameHashCodeAs(foo2NextOtherVal);
	}

	@Test // GH-2151
	void shouldHaveNext() {

		KeysetCursorRequest first = KeysetCursorRequest.ofSize(1, Sort.by("foo"));
		KeysetCursorRequest withNext = first.withNext(Collections.singletonMap("k", "v"));

		assertThat(first.isFirst()).isTrue();
		assertThat(first.hasNext()).isFalse();
		assertThat(first.isLast()).isTrue();

		assertThat(withNext.isFirst()).isTrue();
		assertThat(withNext.hasNext()).isTrue();
		assertThat(withNext.isLast()).isFalse();

		KeysetCursorRequest next = withNext.nextCursorRequest();

		assertThat(next.isFirst()).isFalse();
		assertThat(next.hasNext()).isFalse();
		assertThat(next.isLast()).isTrue();
	}

	@Test // GH-2151
	void withLastShouldResetCursor() {

		KeysetCursorRequest first = KeysetCursorRequest.ofSize(1, Sort.by("foo"));
		KeysetCursorRequest withNext = first.withNext(Collections.singletonMap("k", "v"));

		KeysetCursorRequest last = withNext.withLast(true);

		assertThat(last.hasNext()).isFalse();
		assertThat(last.isLast()).isTrue();
	}
}

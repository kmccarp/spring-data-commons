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

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CursorWindow}.
 *
 * @author Mark Paluch
 */
class CursorWindowUnitTests {

	@Test // GH-2151
	void equalsAndHashCode() {

		KeysetCursorRequest request = KeysetCursorRequest.ofSize(2, Sort.by("bar"));

		CursorWindow<Integer> one = CursorWindow.from(request, List.of(1, 2, 3));
		CursorWindow<Integer> two = CursorWindow.from(request, List.of(1, 2, 3));

		assertThat(one).isEqualTo(two).hasSameHashCodeAs(two);
		assertThat(one.equals(two)).isTrue();

		assertThat(CursorWindow.from(request.withSize(4), List.of(1, 2, 3))).isNotEqualTo(two)
				.doesNotHaveSameHashCodeAs(two);
	}

	@Test // GH-2151
	void allowsIteration() {

		KeysetCursorRequest request = KeysetCursorRequest.ofSize(2, Sort.by("bar"));
		CursorWindow<Integer> window = CursorWindow.from(request, List.of(1, 2, 3));

		for (Integer integer : window) {
			assertThat(integer).isBetween(1, 3);
		}
	}
}

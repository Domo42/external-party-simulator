/*
 * Copyright 2014 Stefan Domnanovits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codebullets.external.party.simulator.pipeline;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Checks whether a blob matches a specific string.
 */
public class GlobMatcher {
    private static final Set<Character> SPECIAL_CHARS;
    private Pattern pattern;

    static {
        Set<Character> specialChars = new HashSet<>();
        specialChars.add('\\');
        specialChars.add('(');
        specialChars.add(')');
        specialChars.add('[');
        specialChars.add(']');
        specialChars.add('{');
        specialChars.add('}');

        SPECIAL_CHARS = Collections.unmodifiableSet(specialChars);
    }

    /**
     * Generates a new instance of GlobMatcher.
     */
    public GlobMatcher(@Nullable final String glob) {
        if (!Strings.isNullOrEmpty(glob)) {
            StringBuilder sb = new StringBuilder();
            sb.append("^");

            for (int i = 0; i < glob.length(); ++i) {
                char c = glob.charAt(i);

                switch (c) {
                    case '*':
                        sb.append(".*");
                        break;

                    case '?':
                        sb.append(".");
                        break;

                    default:
                        if (SPECIAL_CHARS.contains(c)) {
                            sb.append("\\");
                        }

                        sb.append(c);
                        break;
                }
            }

            sb.append("$");
            pattern = Pattern.compile(sb.toString());
        }
    }

    /**
     * Checks whether {@code} text matches the glob pattern.
     * @return True if match; otherwise false.
     */
    public boolean isMatch(final String text) {
        boolean match = false;
        if (pattern == null) {
            match = text == null;
        } else {
            match = pattern.matcher(text).matches();
        }

        return match;
    }
}
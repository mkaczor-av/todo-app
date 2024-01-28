package com.softwaremind.todoappbackend.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class UuidPatternMatcher extends TypeSafeMatcher<String> {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @Override
    protected boolean matchesSafely(String item) {
        return item.matches(UUID_REGEX);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string that matches the UUID pattern");
    }
}

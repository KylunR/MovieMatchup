package edu.tacoma.uw.kylunr.moviematchup;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests the Search Activity to ensure the functionality works
 * and the results are accurate
 */
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityTestRule<SearchActivity> mActivityRule
            = new ActivityTestRule<>(SearchActivity.class);

    /**
     * Tests to see if the search query will result in an
     * accurate result
     */
    @Test
    public void testSearch() {
        String testSearch = "Avengers Endgame";

        onView(withId(R.id.search_bar))
                .perform(typeText(testSearch));
        onView(withId(R.id.search_button))
                .perform(click());

        onView(withText("Avengers: Endgame"))
                .check(matches(isDisplayed()));
    }
}

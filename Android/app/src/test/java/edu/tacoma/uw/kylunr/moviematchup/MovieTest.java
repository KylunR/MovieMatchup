package edu.tacoma.uw.kylunr.moviematchup;


import org.junit.Test;

import edu.tacoma.uw.kylunr.moviematchup.data.Movie;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests the Movie object in creation of a movie object
 */
public class MovieTest {

    /**
     * Tests the movie constructor with valid
     * arguments
     */
    @Test
    public void testMovieConstructor() {
        // Test constructor with good object
        assertNotNull(new Movie("Bad Boys 4", "https://image.tmdb.org/t/p/w185/test", 15681, 10000));
    }

    /**
     * Tests the movie constructor with a bad
     * argument for poster URL
     */
    @Test
    public void testMovieConstructorBadPosterURL() {
        try {
            // Create new movie with bad url
            new Movie("Bad Boys 4", "https://google.com", 15681, 10000);

            // Test fails if successful
            fail("Movie created with bad poster url");

        } catch (IllegalArgumentException e) {

        }
    }
}

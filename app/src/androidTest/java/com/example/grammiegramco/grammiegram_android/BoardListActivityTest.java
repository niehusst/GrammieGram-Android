package com.example.grammiegramco.grammiegram_android;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.v4.content.ContextCompat.getSystemService;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Test the BoardListActivity UI works as expected using Espresso
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class BoardListActivityTest {

    // position of an item below the scree fold that must be recycled
    private static final int ITEM_BELOW_FOLD = 20;

    // set up activity to test
    @Rule
    public IntentsTestRule<BoardListActivity> rule =
            new IntentsTestRule<BoardListActivity>(BoardListActivity.class);

    @Before
    public void setUp() {
        /* TODO: populate the activity recycler with large number of board items (use mockito?)
           each item has board name (R.string.rv_example_text + position)  */
    }

    @Before
    public void stubExternalIntents() {
        // set up intents from external packages to be stubbed
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    /**
     * Test of all the recycler view functions
     * also test that clicked list item takes to BoardActivity
     */
    @Test
    public void recyclerTest() {
        // find recycler view
        ViewInteraction recycler = onView(allOf(withId(R.id.board_recycler), isDisplayed()));

        // scroll to item below the fold
        recycler.perform(actionOnItemAtPosition(ITEM_BELOW_FOLD, scrollTo()));

        // item contains correct info and is visible
        String recyclerText = getApplicationContext().getResources().     //get str resource from file
                getString(R.string.rv_example_text) + String.valueOf(ITEM_BELOW_FOLD); //get text name
        onView(withText(recyclerText)).check(matches(isDisplayed()));

        // clicking item launches BoardActivity
        recycler.perform(actionOnItemAtPosition(ITEM_BELOW_FOLD, click()));

        // check intent is launched to correct place
        intended(hasComponent(BoardActivity.class.getName()));
    }

    /**
     * test that settings button launches correct activity when clicked
     * and passes along necessary data
     */
    @Test
    public void settingsButtonTest() {
        // find button
        ViewInteraction btn = onView(withId(R.id.settings_button)).check(isDisplayed());

        // clicking button launches SettingsFragment
        btn.perform(click());

        // check intent is launched to correct place
        intended(hasComponent(SettingsFragment.class.getName()));
    }

    /**
     * test that logout button launches correct activity when clicked
     * and passes along necessary data
     */
    @Test
    public void logoutButtonTest() {
        // find button
        ViewInteraction btn = onView(withId(R.id.logout_button)).check(isDisplayed());

        // clicking button launches LoginActivity
        btn.perform(click());

        // check intent was launched to correct activity
        intended(hasComponent(LoginActivity.class.getName()));
    }

    /**
     * test that the progress dialogue runs while getting data from server
     */
    @Test
    public void progressDialogueTest() {
        // TODO: cause progress dialogue to start

        // check it is displayed
        onView(withId(R.id.progress_dialogue)).check(isDisplayed());

        // check correct TextView string is displayed with it
        onView(withId(R.id.loading_text)).check(isDisplayed());

        String loadText = getApplicationContext().getResources().   //get str resource from file
                getString(R.string.loading_text);                    //get text name
        onView(withText(loadText)).check(matches(isDisplayed()));

    }

    /**
     * test that correct error info is displayed when there is no/bad wifi
     */
    @Test
    public void wifiErrorTest() {
        // set up mockito spy to check method calls
        IntentsTestRule<BoardListActivity> boardSpy = Mockito.spy(rule);

        // simulate no wifi
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);

        // check image view is displayed
        onView(withId(R.id.error_image)).check(matches(isDisplayed()));

        // check text view with correct string is displayed
        onView(withId(R.id.error_text)).check(isDisplayed());

        String errText = getApplicationContext().getResources().   //get str resource from file
                getString(R.string.wifi_error);                     //get text name
        onView(withText(errText)).check(matches(isDisplayed()));

        // check button is displayed
        ViewInteraction retry = onView(withId(R.id.retry_button)).check(matches(isDisplayed()));

        // check clicking button recalls the API
        Mockito.verify(boardSpy).getActivity().getBoards();
    }

    /**
     * test that correct error info is displayed when there is server error
     * getting data
     */
    @Test
    public void serverErrorTest() {
        // set up mockito spy to check method calls
        IntentsTestRule<BoardListActivity> boardSpy = Mockito.spy(rule);

        // TODO: simulate server err

        // check image view is displayed
        onView(withId(R.id.error_image)).check(matches(isDisplayed()));

        // check text view with correct string is displayed
        onView(withId(R.id.error_text)).check(isDisplayed());

        String errText = getApplicationContext().getResources().   //get str resource from file
                getString(R.string.server_error);                  //get text name
        onView(withText(errText)).check(matches(isDisplayed()));

        // check button with correct text is displayed
        ViewInteraction retry = onView(withId(R.id.retry_button)).check(matches(isDisplayed()));

        // check clicking button recalls the API
        Mockito.verify(boardSpy).getActivity().getBoards();

    }
}

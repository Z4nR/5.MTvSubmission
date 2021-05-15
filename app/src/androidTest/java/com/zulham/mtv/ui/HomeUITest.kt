package com.zulham.mtv.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.contrib.ViewPagerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zulham.mtv.R
import com.zulham.mtv.ui.main.MainActivity
import com.zulham.mtv.utils.DummyData
import com.zulham.mtv.utils.IdlingResource
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeUITest {

    private val listItemInTest = 1

    private val movieInTest = DummyData.generateDummyMovie()

    private val tvInTest = DummyData.generateDummyTV()

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
    }

    @Test
    fun test_isFragment_onLaunch() {
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()))
        onView(withId(R.id.view_pager))
                .perform(ViewPagerActions.scrollRight(true))
    }

    @Test
    fun loadMovie(){
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvMovie)))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(movieInTest.size))
    }

    @Test
    fun loadTV(){
        onView(withId(R.id.view_pager))
                .perform(ViewPagerActions.scrollRight(true))
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvMovie)))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(tvInTest.size))
    }

    @Test
    fun detailMovie(){
        onView(allOf(isDisplayed(), withId(R.id.rvMovie)))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(listItemInTest, click()))

        onView(withId(R.id.titleDetail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.genreDetail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showId))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showProduction))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tv_justified_paragraph))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fabDetail))
            .perform(click())
    }

    @Test
    fun detailTVShow(){
        onView(withId(R.id.view_pager))
                .perform(ViewPagerActions.scrollRight(true))
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvMovie)))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(listItemInTest, click()))

        onView(withId(R.id.titleDetail))
                .check(matches(isDisplayed()))

        onView(withId(R.id.genreDetail))
                .check(matches(isDisplayed()))

        onView(withId(R.id.showId))
                .check(matches(isDisplayed()))

        onView(withId(R.id.showProduction))
                .check(matches(isDisplayed()))

        onView(withId(R.id.tv_justified_paragraph))
                .check(matches(isDisplayed()))

        onView(withId(R.id.fabDetail))
            .perform(click())
    }

    @Test
    fun loadMovieFav(){
        onView(withId(R.id.fabMenu))
            .perform(click())
        onView(withId(R.id.tabs))
            .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvFavorite)))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(movieInTest.size))
        onView(allOf(isDisplayed(), withId(R.id.rvFavorite)))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, swipeLeft()))
    }

    @Test
    fun loadTVFav(){
        onView(withId(R.id.fabMenu))
            .perform(click())
        onView(withId(R.id.view_pager))
            .perform(ViewPagerActions.scrollRight(true))
        onView(withId(R.id.tabs))
            .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvFavorite)))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(tvInTest.size))

    }

    @Test
    fun loadDetailMovieFav(){
        onView(withId(R.id.fabMenu))
            .perform(click())

        onView(allOf(isDisplayed(), withId(R.id.rvFavorite)))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.titleDetailFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.genreDetailFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showIdFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showProductionFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tv_justified_paragraphFav))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loadDetailTVFav(){
        onView(withId(R.id.fabMenu))
            .perform(click())

        onView(withId(R.id.view_pager))
            .perform(ViewPagerActions.scrollRight(true))
        onView(withId(R.id.tabs))
            .check(matches(isDisplayed()))
        onView(allOf(isDisplayed(), withId(R.id.rvFavorite)))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.titleDetailFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.genreDetailFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showIdFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.showProductionFav))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tv_justified_paragraphFav))
            .check(matches(isDisplayed()))
    }

}
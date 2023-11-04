package com.example.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataDummy
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.UserStoryRepository
import com.example.storyapp.data.local.room.entity.StoriesEntity
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.ui.adapter.StoriesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userStoryRepository: UserStoryRepository

    private lateinit var mainViewModel: MainViewModel
    private val token = "Bearer token"
    @Before
    fun setUp(){
        mainViewModel = MainViewModel(userStoryRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDataDummyEntity()
        val data = StoryPagingSource.snapshot(dummyStory)
        val expectedStories = MutableLiveData<PagingData<StoriesEntity>>()
        expectedStories.value = data
        Mockito.`when`(userStoryRepository.getAllStories(token)).thenReturn(expectedStories)

        val actualStories = mainViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<StoriesEntity> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoriesEntity>>()
        expectedStory.value = data
        Mockito.`when`(userStoryRepository.getAllStories(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(userStoryRepository)
        val actualStory = mainViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoriesEntity>>>(){
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(data: List<StoriesEntity>): PagingData<StoriesEntity> {
            return PagingData.from(data)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
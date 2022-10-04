package com.navigation.latihan.storyappsubmission1intermediateandroid.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.navigation.latihan.storyappsubmission1intermediateandroid.api.InterfaceApi
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb.DbStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb.RemoteKey

@OptIn(ExperimentalPagingApi::class)
class RemoteModeratorStory(private val dbStory: DbStory, private val interfaceApi: InterfaceApi, private val token: String) : RemoteMediator<Int, StoryApp>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryApp>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getKeyClosePosition(state)
                remoteKey?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKey = getKeyForFirstItem(state)
                val keyPrev = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null)
                keyPrev
            }
            LoadType.APPEND -> {
                val remoteKey = getKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }
        return try {
            val dataRespon =
                interfaceApi.getStoryApp("Bearer $token", page, state.config.pageSize).listStory
            val endOfPaginationReached = dataRespon.isEmpty()

            dbStory.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbStory.daoKeyRemote().deleteKeyRemote()
                    dbStory.daoStory().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = dataRespon.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                dbStory.daoKeyRemote().insertAllStory(keys)
                dbStory.daoStory().createStory(dataRespon)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }


    private suspend fun getKeyForLastItem(stateRemote: PagingState<Int, StoryApp>): RemoteKey? {
        return stateRemote.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            dbStory.daoKeyRemote().getKeyRemoteId(it.id)
        }
    }

    private suspend fun getKeyForFirstItem(stateRemote: PagingState<Int, StoryApp>): RemoteKey? {
        return stateRemote.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            dbStory.daoKeyRemote().getKeyRemoteId(it.id)
        }
    }

    private suspend fun getKeyClosePosition(stateRemote: PagingState<Int, StoryApp>): RemoteKey? {
        return stateRemote.anchorPosition?.let { position ->
            stateRemote.closestItemToPosition(position)?.id?.let {
                dbStory.daoKeyRemote().getKeyRemoteId(it)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE= 1
    }

}
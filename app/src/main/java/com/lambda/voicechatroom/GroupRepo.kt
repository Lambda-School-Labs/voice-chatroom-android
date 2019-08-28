package com.lambda.voicechatroom

import androidx.lifecycle.MutableLiveData
import java.util.*

class GroupRepo {

    private var liveDataList: MutableLiveData<ArrayList<Group>> = MutableLiveData()
    private var dataList: ArrayList<Group> = arrayListOf()

    fun initList(): MutableLiveData<ArrayList<Group>> {
        return liveDataList
    }

    suspend fun getGroups() {
        dataList = ApiDao.getGroups() as ArrayList<Group>
        liveDataList.postValue(dataList)
    }

    suspend fun addGroup(name: String) {
        val group = ApiDao.createGroup(name)
        if (group != null) {
            dataList.add(group)
            liveDataList.postValue(dataList)
        }
    }

}
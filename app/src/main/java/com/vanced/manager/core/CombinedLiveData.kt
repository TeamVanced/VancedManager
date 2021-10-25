package com.vanced.manager.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * CombinedLiveData is a helper class to combine results from two LiveData sources.
 * @param combine   Function reference that will be used to combine all LiveData data.
 * @param R         The type of data returned after combining all LiveData data.
 * Usage:
 * CombinedLiveData(
 *     getLiveData1(),
 *     getLiveData2()
 * ) { data1, data2 ->
 *     // Use datas[0], datas[1], ..., datas[N] to return a value
 * }
 */
class CombinedLiveData<R, A, B>(
    liveDataA: LiveData<A>,
    liveDataB: LiveData<B>,
    private val combine: (a: A?, b: B?) -> R
) : MediatorLiveData<R>() {

    private var a: A? = null
    private var b: B? = null

    init {
        addSource(liveDataA) {
            a = it
            value = combine(a, b)
        }
        addSource(liveDataB) {
            b = it
            value = combine(a, b)
        }
    }
}
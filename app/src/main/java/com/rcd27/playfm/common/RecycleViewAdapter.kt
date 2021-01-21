package com.rcd27.playfm.common

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

/**
 * Подойдёт для 99% всех [androidx.recyclerview.widget.RecyclerView]
 * Под капотом обвязка вокруг [androidx.recyclerview.widget.ListAdapter]
 *
 * see: https://github.com/sockeqwe/AdapterDelegates
 */

// TODO: для реализации анимаций при смене списка(сортировке), см. AsyncListDifferDelegationAdapter
class RecycleViewAdapter<T : ViewObject>(vararg items: AdapterDelegate<List<T>>) :
    ListDelegationAdapter<List<T>>(AdapterDelegatesManager()) {

    init {
        items.forEach {
            super.delegatesManager.addDelegate(it)
        }
    }

    fun submitList(data: List<T>) {
        items = data
        notifyDataSetChanged()
    }
}

/**
 * Маркер-интерфейс для передачи в делегат [RecycleViewAdapter].
 */
interface ViewObject
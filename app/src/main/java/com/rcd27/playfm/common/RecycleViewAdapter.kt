package com.rcd27.playfm.common

import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

/**
 * Подойдёт для 99% всех [androidx.recyclerview.widget.RecyclerView]
 * Под капотом обвязка вокруг [androidx.recyclerview.widget.ListAdapter]
 *
 * see: https://github.com/sockeqwe/AdapterDelegates
 */

// TODO: для реализации анимаций при смене списка(сортировке), см. AsyncListDifferDelegationAdapter
class RecycleViewAdapter : ListDelegationAdapter<List<DisplayableItem>>(AdapterDelegatesManager()) {
    val delegatesManager: AdapterDelegatesManager<List<DisplayableItem>>
        get() = super.delegatesManager
}

/**
 * Маркер-интерфейс для передачи в делегат [RecycleViewAdapter].
 */
interface DisplayableItem
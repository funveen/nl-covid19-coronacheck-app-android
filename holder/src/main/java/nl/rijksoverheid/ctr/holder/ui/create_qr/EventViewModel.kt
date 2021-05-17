package nl.rijksoverheid.ctr.holder.ui.create_qr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.rijksoverheid.ctr.holder.ui.create_qr.usecases.EventsResult
import nl.rijksoverheid.ctr.holder.ui.create_qr.usecases.GetEventsUseCase
import nl.rijksoverheid.ctr.holder.ui.create_qr.usecases.SaveEventsUseCase
import nl.rijksoverheid.ctr.shared.livedata.Event

/*
 *  Copyright (c) 2021 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *   Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *   SPDX-License-Identifier: EUPL-1.2
 *
 */
abstract class EventViewModel : ViewModel() {
    val loading: LiveData<Event<Boolean>> = MutableLiveData()
    val eventsResult: LiveData<Event<EventsResult>> = MutableLiveData()
    val savedEvents: LiveData<Event<Boolean>> = MutableLiveData()

    abstract fun getRetrievedResult(): EventsResult.Success?
    abstract fun getEvents(digidToken: String)
    abstract fun saveEvents()
}

class EventViewModelImpl(
    private val eventUseCase: GetEventsUseCase,
    private val saveEventsUseCase: SaveEventsUseCase
) : EventViewModel() {
    override fun getRetrievedResult(): EventsResult.Success? {
        return eventsResult.value?.peekContent() as? EventsResult.Success
    }

    override fun getEvents(digidToken: String) {
        (loading as MutableLiveData).value = Event(true)
        viewModelScope.launch {
            try {
                (eventsResult as MutableLiveData).value =
                    Event(eventUseCase.getVaccinationEvents(digidToken))
            } finally {
                loading.value = Event(false)
            }
        }
    }

    override fun saveEvents() {
        (loading as MutableLiveData).value = Event(true)
        viewModelScope.launch {
            try {
                getRetrievedResult()?.let {
                    saveEventsUseCase.save(it.signedModels)
                }
                (savedEvents as MutableLiveData).value = Event(true)
            } finally {
                loading.value = Event(false)
            }
        }
    }
}

package nl.rijksoverheid.ctr.holder.ui.myoverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.rijksoverheid.ctr.holder.persistence.database.entities.GreenCardType
import nl.rijksoverheid.ctr.holder.ui.create_qr.usecases.QrCodeDataUseCase
import nl.rijksoverheid.ctr.holder.ui.myoverview.models.QrCodeData

abstract class QrCodeViewModel : ViewModel() {
    val qrCodeDataLiveData = MutableLiveData<QrCodeData>()
    abstract fun generateQrCode(type: GreenCardType, size: Int, credential: ByteArray, shouldDisclose: Boolean)
}

class QrCodeViewModelImpl(private val qrCodeDataUseCase: QrCodeDataUseCase) : QrCodeViewModel() {

    override fun generateQrCode(
        type: GreenCardType,
        size: Int,
        credential: ByteArray,
        shouldDisclose: Boolean
    ) {

        viewModelScope.launch {
            val qrCodeData = qrCodeDataUseCase.getQrCodeData(
                greenCardType = type,
                credential = credential,
                qrCodeWidth = size,
                qrCodeHeight = size,
                shouldDisclose = shouldDisclose
            )

            qrCodeDataLiveData.postValue(qrCodeData)
        }
    }
}
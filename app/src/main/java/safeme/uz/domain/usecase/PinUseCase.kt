package safeme.uz.domain.usecase

interface PinUseCase {
    fun hasPinCode() : Boolean

    fun saveNewPin(pin : String) : Boolean

    fun getCurrentPin() : String
}
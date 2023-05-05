package safeme.uz.domain.usecase.impl

import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.PinUseCase
import javax.inject.Inject

class PinUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : PinUseCase {

    override fun hasPinCode(): Boolean {
        return authRepository.hasPinCode()
    }

    override fun saveNewPin(pin: String): Boolean {
        return authRepository.saveNewPin(pin)
    }

    override fun getCurrentPin(): String {
        return authRepository.getCurrentPin()
    }
}
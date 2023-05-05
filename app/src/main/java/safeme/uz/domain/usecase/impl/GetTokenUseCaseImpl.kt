package safeme.uz.domain.usecase.impl

import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.GetTokenUseCase
import javax.inject.Inject

class GetTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : GetTokenUseCase {
    override fun invoke(): Boolean {
        return authRepository.getToken()
    }
}
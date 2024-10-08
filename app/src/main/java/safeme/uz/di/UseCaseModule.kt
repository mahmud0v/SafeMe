package safeme.uz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import safeme.uz.domain.LogOutUseCase
import safeme.uz.domain.usecase.*
import safeme.uz.domain.usecase.impl.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @[Binds Singleton]
    fun getRegisterUseCase(impl: RegisterUseCaseImpl): RegisterUseCase

    @[Binds Singleton]
    fun getVerifyRegisterUseCase(impl: VerifyRegisterUseCaseImpl): VerifyRegisterUseCase

    @[Binds Singleton]
    fun getLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @[Binds Singleton]
    fun resetPinCode(impl: ResetPinCodeUseCaseImpl): ResetPinCodeUseCase

    @[Binds Singleton]
    fun getHasPinCodeUseCase(impl: PinUseCaseImpl): PinUseCase

    @[Binds Singleton]
    fun getTokenUseCase(impl: GetTokenUseCaseImpl): GetTokenUseCase

    @[Binds Singleton]
    fun getForgetPasswordUseCase(impl: ForgetPasswordUseCaseImpl): ForgetPasswordUseCase

    @[Binds Singleton]
    fun getSendUserData(impl: SendUserDataUseCaseImpl): SendUserDataUseCase

    @[Binds Singleton]
    fun getAddingChildData(impl: AddingChildDataUseCaseImpl): AddingChildDataUseCase

    @[Binds Singleton]
    fun getVerifyResetPassword(impl: VerifyResetPasswordUseCaseImpl): VerifyResetPasswordUseCase

    @[Binds Singleton]
    fun verifyPinCode(impl: VerifyPinCodeCaseImpl): VerifyPinCodeUseCase

    @[Binds Singleton]
    fun getResetPasswordUseCase(impl: ResetPasswordUseCaseImpl): ResetPasswordUseCase

    @[Binds Singleton]
    fun getGetRegionsUseCase(impl: GetRegionsUseCaseImpl): GetRegionsUseCase

    @[Binds Singleton]
    fun getGetDistrictsByIdUseCase(impl: GetDistrictsByIdUseCaseImpl): GetDistrictsByIdUseCase

    @[Binds Singleton]
    fun getGetMFYsByIdUseCase(impl: GetMFYsByIdUseCaseImpl): GetMFYsByIdUseCase

    @[Binds Singleton]
    fun getGetDistrictsUseCase(impl: GetDistrictsUseCaseImpl): GetDistrictsUseCase

    @[Binds Singleton]
    fun getGetMFYsUseCase(impl: GetMFYsUseCaseImpl): GetMFYsUseCase

    @[Binds Singleton]
    fun getLogOutUseCase(impl: LogOutUseCaseImpl): LogOutUseCase

    @[Binds Singleton]
    fun getAllCategories(impl: GetAllCategoriesUseCaseImpl): GetAllCategoriesUseCase

    @[Binds Singleton]
    fun getUserData(impl: ProfileUseCaseImpl): ProfileUseCase

    @[Binds Singleton]
    fun userUpdate(impl: UserUpdateUseCaseImpl): UserUpdateUseCase

    @[Binds Singleton]
    fun getInspectorData(impl: GetInspectorDataUseCaseImpl): GetInspectorDataUseCase

    @[Binds Singleton]
    fun sosNotified(impl: SosNotifiedUseCaseImpl): SosNotifiedUseCase

    @[Binds Singleton]
    fun giveAppeal(impl: GiveAppealUseCaseImpl): GiveAppealUseCase

    @[Binds Singleton]
    fun pollQuestion(impl: PollQuestionUseCaseImpl): PollQuestionUseCase

    @[Binds Singleton]
    fun getPollById(impl: GetPollByIdUseCaseImpl): GetPollByIdUseCase

    @[Binds Singleton]
    fun giveAnswer(impl: GivePollAnswerUseCaseImpl): GivePollAnswerUseCase

    @[Binds Singleton]
    fun accessTokenUpdate(impl:TokenUpdateUseCaseImpl):TokenUpdateUseCase

}
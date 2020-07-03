package com.dlet.cartrack.challenge.testcases

import com.dlet.cartrack.challenge.domain.exceptions.InvalidUsernameAndPasswordException
import com.dlet.cartrack.challenge.domain.exceptions.RequiredFieldException
import com.dlet.cartrack.challenge.domain.sealedclass.DataResult
import com.dlet.cartrack.challenge.domain.usecase.LogInUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.amshove.kluent.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

  @Test
  fun loginSuccessTest() {
    //GIVEN
    val mockLogInUseCase = mock<LogInUseCase>()

    doReturn(Single.just(DataResult.Success(TestData.user1)))
      .`when`(mockLogInUseCase)
      .doLogIn(TestData.user1.username, TestData.user1.password!!)

    //WHEN
    val result = mockLogInUseCase.doLogIn(TestData.user1.username, TestData.user1.password!!).test()


    //THEN
    verify(mockLogInUseCase).doLogIn(TestData.user1.username, TestData.user1.password!!)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Success(TestData.user1))
  }

  @Test
  fun usernameRequiredTest() {
    //GIVEN
    val mockLogInUseCase = mock<LogInUseCase>()
    val throwable = RequiredFieldException("Username")

    doReturn(Single.just(DataResult.Failed(throwable)))
      .`when`(mockLogInUseCase)
      .doLogIn("", TestData.user1.password!!)

    //WHEN
    val result = mockLogInUseCase.doLogIn("", TestData.user1.password!!).test()


    //THEN
    verify(mockLogInUseCase).doLogIn("", TestData.user1.password!!)

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))
  }

  @Test
  fun passwordRequiredTest() {
    //GIVEN
    val mockLogInUseCase = mock<LogInUseCase>()
    val throwable = RequiredFieldException("Password")

    doReturn(Single.just(DataResult.Failed(throwable)))
      .`when`(mockLogInUseCase)
      .doLogIn(TestData.user1.username, "")

    //WHEN
    val result = mockLogInUseCase.doLogIn(TestData.user1.username, "").test()


    //THEN
    verify(mockLogInUseCase).doLogIn(TestData.user1.username, "")

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))
  }

  @Test
  fun invalidUsernameAndPasswordRequiredTest() {
    //GIVEN
    val mockLogInUseCase = mock<LogInUseCase>()
    val throwable = InvalidUsernameAndPasswordException()

    doReturn(Single.just(DataResult.Failed(throwable)))
      .`when`(mockLogInUseCase)
      .doLogIn("tes", "test")

    //WHEN
    val result = mockLogInUseCase.doLogIn("tes", "test").test()


    //THEN
    verify(mockLogInUseCase).doLogIn("tes", "test")

    result.assertNoErrors()
    result.assertComplete()
    result.assertValueCount(1)
    result.assertValue(DataResult.Failed(throwable))
  }
}
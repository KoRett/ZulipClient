package com.korett.zulip_client.core.domain.use_cases

import com.korett.zulip_client.core.domain.model.UserModel
import com.korett.zulip_client.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(query: String): Flow<List<UserModel>> =
        userRepository.getAllUsers().map { userList ->
            userList.filter { user ->
                query.lowercase() in user.username.lowercase()
            }
        }
}
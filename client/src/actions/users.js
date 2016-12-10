import {
    request,
} from '../utils/apiUtils';

export const LOAD_USERS_REQUEST = 'LOAD_USERS_REQUEST';
export const LOAD_USERS_SUCCESS = 'LOAD_USERS_SUCCESS';
export const LOAD_USERS_FAILURE = 'LOAD_USERS_FAILURE';

/**
 * Action which loads all users from back-end.
 */
export function loadUsers() {
    return dispatch => {
        return dispatch(request(
            '/admin/accounts/', {
                method: 'get'
            },
            loadUsersRequest(),
            loadUsersSuccess,
            loadUsersFailure));
    }
}

/**
 * Action which changes the enabled or disabled state of the user on back-end.
 */
export function enableUser(id, enable) {
    return dispatch => {
        return dispatch(request(
            `/admin/accounts/${id}`, {
                method: 'put',
                body: JSON.stringify({
                    enabled: enable
                })
            },
            null,
            loadUsers,
            loadUsers));
    }
}

function loadUsersRequest() {
    return {
        type: LOAD_USERS_REQUEST
    }
}

function loadUsersSuccess(users) {
    return {
        type: LOAD_USERS_SUCCESS,
        users: users.map((user) => {
            return {
                id: user.id,
                role: user.role === 'ROLE_ADMINISTRATOR'
                    ? 'Administrator'
                    : 'User',
                username: user.username,
                enabled: user.enabled
            }
        })
    }
}

function loadUsersFailure(error) {
    return {
        type: LOAD_USERS_FAILURE,
        error
    }
}
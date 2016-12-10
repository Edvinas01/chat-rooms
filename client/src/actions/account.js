import {
    removeToken,
    setToken,
    request,
    TOKEN
} from '../utils/apiUtils';

export const LOAD_PROFILE_REQUEST = 'LOAD_PROFILE_REQUEST';
export const LOAD_PROFILE_SUCCESS = 'LOAD_PROFILE_SUCCESS';
export const LOAD_PROFILE_FAILURE = 'LOAD_PROFILE_FAILURE';

export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';

export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';

export const REGISTER_REQUEST = 'REGISTER_REQUEST';
export const REGISTER_SUCCESS = 'REGISTER_SUCCESS';
export const REGISTER_FAILURE = 'REGISTER_FAILURE';
export const REGISTER_CLEAR = 'REGISTER_CLEAR';

/**
 * Action which loads currently authenticated users profile.
 */
export function loadProfile(current) {
    return dispatch => {
        if (!current.loggedIn || current.user || current.loadingProfile) {
            return dispatch;
        }
        return dispatch(request(
            '/accounts', {
                method: 'get'
            },
            loadProfileRequest(),
            loadProfileSuccess,
            loadProfileFailure));
    }
}

function loadProfileRequest() {
    return {
        type: LOAD_PROFILE_REQUEST
    }
}

function loadProfileSuccess(profile) {
    return {
        type: LOAD_PROFILE_SUCCESS,
        user: {
            id: profile.id,
            username: profile.username,
            enabled: profile.enabled,
            role: profile.role
        }
    }
}

function loadProfileFailure(error) {
    removeToken();
    return {
        type: LOAD_PROFILE_FAILURE,
        error
    }
}

/**
 * Login action.
 */
export function login(username, password) {
    return request(
        '/accounts/auth', {
            method: 'post',
            body: JSON.stringify({
                username,
                password
            })
        },
        loginRequest(),
        loginSuccess,
        loginFailure);
}

/**
 * Action which sends a login error message.
 */
export function loginError(message) {
    return loginFailure({
        message: message
    });
}

function loginRequest() {
    return {
        type: LOGIN_REQUEST
    };
}

function loginSuccess(payload) {
    setToken(payload[TOKEN]);
    return {
        type: LOGIN_SUCCESS
    }
}

function loginFailure(error) {
    return {
        type: LOGIN_FAILURE,
        error
    };
}

/**
 * Logout action.
 */
export function logout() {
    return request(
        '/accounts/logout', {
            method: 'post'
        },
        logoutRequest(),
        logoutSuccess,
        logoutFailure);
}

function logoutRequest() {
    return {
        type: LOGOUT_REQUEST
    };
}

function logoutSuccess() {
    removeToken();
    return {
        type: LOGOUT_SUCCESS
    }
}

function logoutFailure(error) {
    return {
        type: LOGOUT_FAILURE,
        error
    };
}

/**
 * Action which sets registration failure message.
 */
export function registerFailureMessage(message) {
    return registerFailure({
        message: message
    });
}

/**
 * Action which clears registration details.
 */
export function registerClear() {
    return {
        type: REGISTER_CLEAR
    }
}

/**
 * New account registration action.
 */
export function register(username, password) {
    return request(
        '/accounts', {
            method: 'post',
            body: JSON.stringify({
                username: username,
                password: password
            })
        },
        registerRequest(),
        registerSuccess,
        registerFailure);
}

function registerRequest() {
    return {
        type: REGISTER_REQUEST
    };
}

function registerSuccess(profile) {
    return {
        type: REGISTER_SUCCESS,
        profile
    }
}

function registerFailure(error) {
    return {
        type: REGISTER_FAILURE,
        error
    };
}
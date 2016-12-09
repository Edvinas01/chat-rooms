import {
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT_SUCCESS,
    LOAD_PROFILE_FAILURE
} from '../actions/account';

import {
    getToken
} from '../utils/apiUtils'

function initializeState() {
    const token = getToken();

    return Object.assign({}, {
        loggingIn: false,
        loggedIn: token != null,
        error: null
    });
}

export default function login(state = initializeState(), action) {
    switch (action.type) {
        case LOGIN_REQUEST:
            return Object.assign({}, state, {loggingIn: true, loggedIn: false});
        case LOGIN_SUCCESS:
            return Object.assign({}, state, {loggingIn: false, loggedIn: true, error: null});
        case LOGIN_FAILURE:
            return Object.assign({}, state, {loggingIn: false, loggedIn: false, error: action.error});
        case LOAD_PROFILE_FAILURE:
        case LOGOUT_SUCCESS:
            return Object.assign({}, state, {loggingIn: false, loggedIn: false});
        default:
            return state;
    }
}
import {
    LOAD_USERS_REQUEST,
    LOAD_USERS_SUCCESS,
    LOAD_USERS_FAILURE
} from '../actions/users';

function initializeState() {
    return Object.assign({}, {
        loading: false,
        users: []
    });
}

export default function users(state = initializeState(), action = {}) {
    switch (action.type) {
        case LOAD_USERS_REQUEST:
            return {...state, loading: true};
        case LOAD_USERS_SUCCESS:
            return {...state, users: action.users, loading: false};
        case LOAD_USERS_FAILURE:
            return {...state, error: action.error, loading: false};
        default:
            return state;
    }
}
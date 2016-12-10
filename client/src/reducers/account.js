import {
    LOAD_PROFILE_REQUEST,
    LOAD_PROFILE_SUCCESS,
    LOAD_PROFILE_FAILURE,
    LOGOUT_SUCCESS
} from '../actions/account';

function initializeState() {
    return Object.assign({}, {
        loading: false,
        user: null
    });
}

export default function account(state = initializeState(), action = {}) {
    switch (action.type) {
        case LOAD_PROFILE_REQUEST:
            return Object.assign({}, state, {loading: true});
        case LOAD_PROFILE_SUCCESS:
            const user = action.user;
            user.admin = user.role === 'ROLE_ADMINISTRATOR';

            return Object.assign({}, state, {
                user: user, loading: false
            });
        case LOAD_PROFILE_FAILURE:
            return Object.assign({}, state, {user: null, loading: false});
        case LOGOUT_SUCCESS:
            return Object.assign({}, state, {user: null});
        default:
            return state;
    }
}
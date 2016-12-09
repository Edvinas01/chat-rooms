import {
    REGISTER_REQUEST,
    REGISTER_SUCCESS,
    REGISTER_FAILURE,
    REGISTER_CLEAR
} from '../actions/account';

function initializeState() {
    return Object.assign({}, {
        registering: false,
        error: null,
        user: null
    });
}

export default function login(state = initializeState(), action) {
    switch (action.type) {
        case REGISTER_REQUEST:
            return Object.assign({}, state, {registering: true});
        case REGISTER_SUCCESS:
            console.log(action);
            return Object.assign({}, state, {registering: false, error: null, user: action.profile});
        case REGISTER_FAILURE:
            return Object.assign({}, state, {registering: false, error: action.error});
        case REGISTER_CLEAR:
            return null;
        default:
            return state;
    }
}
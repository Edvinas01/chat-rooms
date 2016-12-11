import {
    LOAD_CHANNELS_SUCCESS,
    DELETE_CHANNEL_SUCCESS,
    DELETE_CHANNEL_FAILURE,
    CREATE_CHANNEL_FAILURE
} from '../actions/channel';

function initializeState() {
    return Object.assign({}, {
        error: null,
        channels: []
    });
}

export default function channels(state = initializeState(), action = {}) {
    switch (action.type) {
        case LOAD_CHANNELS_SUCCESS:
            return {
                ...state,
                error: null,
                channels: action.channels.map((channel) => {
                    return {
                        id: channel.id,
                        name: channel.name,
                        commentCount: channel.commentCount
                    }
                })
            };
        case DELETE_CHANNEL_SUCCESS:
            return {
                ...state,
                error: null
            };
        case DELETE_CHANNEL_FAILURE:
        case CREATE_CHANNEL_FAILURE:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
}
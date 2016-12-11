import {
    LOAD_CHANNEL_SUCCESS,
    LOAD_CHANNEL_FAILURE,
    LOAD_COMMENTS_SUCCESS,
    LOAD_COMMENTS_FAILURE
} from '../actions/channel';

function initializeState() {
    return Object.assign({}, {
        error: null,
        channel: {
            id: null,
            name: null,
            commentCount: null,
            comments: []
        }
    });
}

export default function channel(state = initializeState(), action = {}) {
    switch (action.type) {
        case LOAD_CHANNEL_SUCCESS:
            const {channel} = action;
            return {
                ...state,
                error: null,
                channel: {
                    ...state.channel,
                    id: channel.id,
                    name: channel.name,
                    commentCount: channel.commentCount
                }
            };
        case LOAD_COMMENTS_SUCCESS:
            const mappedComments = action.comments.map(function (comment) {
                return {
                    sentOn: new Date(comment.sentOn),
                    username: comment.username,
                    message: comment.message
                }
            });

            return {
                ...state,
                channel: {
                    ...state.channel,
                    comments: mappedComments
                },
                error: null
            };
        case LOAD_CHANNEL_FAILURE:
        case LOAD_COMMENTS_FAILURE:
            return {...state, loading: false, error: action.error};
        default:
            return state;
    }
}
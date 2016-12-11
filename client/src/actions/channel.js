import {
    request,
} from '../utils/apiUtils';

export const LOAD_CHANNEL_SUCCESS = 'LOAD_CHANNEL_SUCCESS';
export const LOAD_CHANNEL_FAILURE = 'LOAD_CHANNEL_FAILURE';

export const LOAD_COMMENTS_SUCCESS = 'LOAD_COMMENTS_SUCCESS';
export const LOAD_COMMENTS_FAILURE = 'LOAD_COMMENTS_FAILURE';

export const SEND_COMMENT_SUCCESS = 'SEND_COMMENT_SUCCESS';
export const SEND_COMMENT_FAILURE = 'SEND_COMMENT_FAILURE';

export const LOAD_CHANNELS_SUCCESS = 'LOAD_CHANNELS_SUCCESS';
export const LOAD_CHANNELS_FAILURE = 'LOAD_CHANNELS_FAILURE';

export const CREATE_CHANNEL_SUCCESS = 'CREATE_CHANNEL_SUCCESS';
export const CREATE_CHANNEL_FAILURE = 'CREATE_CHANNEL_FAILURE';

export const DELETE_CHANNEL_SUCCESS = 'DELETE_CHANNEL_SUCCESS';
export const DELETE_CHANNEL_FAILURE = 'DELETE_CHANNEL_FAILURE';

const COMMENT_LOAD_COUNT = 100;

/**
 * Action which loads channel information by id.
 */
export function loadChannel(id) {
    return dispatch => {
        return dispatch(request(
            `/channels/${id}`, {
                method: 'get'
            },
            null,
            loadChannelSuccess,
            loadChannelFailure));
    }
}

function loadChannelSuccess(channel) {
    return {
        type: LOAD_CHANNEL_SUCCESS,
        channel
    }
}

function loadChannelFailure(error) {
    return {
        type: LOAD_CHANNEL_FAILURE,
        error
    }
}

/**
 * Load a part of channel comments.
 */
export function loadComments(id) {
    return dispatch => {
        return dispatch(request(
            `/channels/${id}/comments?count=${COMMENT_LOAD_COUNT}`, {
                method: 'get'
            },
            null,
            loadCommentsSuccess,
            loadCommentsFailure));
    }
}

function loadCommentsSuccess(comments) {
    return {
        type: LOAD_COMMENTS_SUCCESS,
        comments
    }
}

function loadCommentsFailure(error) {
    return {
        type: LOAD_COMMENTS_FAILURE,
        error
    }
}

/**
 * Send a comment to a specific channel.
 */
export function sendComment(id, comment) {
    return dispatch => {
        return dispatch(request(
            `/channels/${id}/comments`, {
                method: 'post',
                body: JSON.stringify({
                    message: comment
                })
            },
            null,
            (comment) => {
                dispatch(loadComments(id));
                return {
                    type: SEND_COMMENT_SUCCESS,
                    comment
                }
            },
            sendCommentFailure));
    }
}

function sendCommentFailure(error) {
    return {
        type: SEND_COMMENT_FAILURE,
        error
    }
}

/**
 * Load a number of channels.
 */
export function loadChannels() {
    return dispatch => {
        return dispatch(request(
            '/channels', {
                method: 'get'
            },
            null,
            (channels) => {
                return {
                    type: LOAD_CHANNELS_SUCCESS,
                    channels
                }
            },
            (error) => {
                return {
                    type: LOAD_CHANNELS_FAILURE,
                    error
                }
            })
        );
    }
}

/**
 * Create a new channel.
 */
export function createChannel(name) {
    return dispatch => {
        return dispatch(request(
            '/admin/channels', {
                method: 'post',
                body: JSON.stringify({
                    name: name
                })
            },
            null,
            (channel) => {
                dispatch(loadChannels());
                return {
                    type: CREATE_CHANNEL_SUCCESS,
                    channel
                }
            },
            (error) => {
                return {
                    type: CREATE_CHANNEL_FAILURE,
                    error
                }
            })
        );
    }
}

/**
 * Delete channel by id.
 */
export function deleteChannel(id) {
    return dispatch => {
        return dispatch(request(
            `/admin/channels/${id}`, {
                method: 'delete'
            },
            null,
            () => {
                dispatch(loadChannels());
                return {
                    type: DELETE_CHANNEL_SUCCESS
                }
            },
            (error) => {
                return {
                    type: DELETE_CHANNEL_FAILURE,
                    error
                }
            })
        );
    }
}
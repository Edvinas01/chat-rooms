import React, {Component, PropTypes} from 'react';
import {connect} from 'react-redux';
import {
    loadComments,
    loadChannel,
    sendComment
} from '../../actions/channel'

import Messages from '../../components/chat/Messages'

const UPDATE_RATE_MS = 1000;

class Chat extends Component {
    constructor(props) {
        super(props);

        this.state = {
            refreshId: null,
            message: ''
        };

        this.onMessageBoxKeyDown = this.onMessageBoxKeyDown.bind(this);
        this.onMessageBoxChange = this.onMessageBoxChange.bind(this);
    }

    componentWillMount() {
        if (!this.props.user) {
            this.context.router.replace('/');
        }
    }

    componentDidMount() {
        const {id} = this.props.params;

        this.props.dispatch(loadChannel(id));
        this.props.dispatch(loadComments(id)); // Load initially.

        this.setState({
            refreshId: setInterval(() => {
                // Load comments every few seconds.
                this.props.dispatch(loadComments(id));
            }, UPDATE_RATE_MS)
        });
    }

    componentWillUnmount() {
        const {refreshId} = this.state;
        if (refreshId) {
            clearInterval(refreshId);
        }
    }

    onMessageBoxKeyDown(e) {
        if (e.keyCode === 13 && !e.shiftKey) {
            const {id} = this.props.params;
            const value = e.target.value.trim();

            if (value.length > 0) {
                e.preventDefault(); // Fixes an issue where extra new line is added.

                this.props.dispatch(sendComment(id, value));
                this.setState({
                    message: ''
                });
            }
        }
    }

    onMessageBoxChange(e) {
        this.setState({
            message: e.target.value
        });
    }

    render() {
        const {channel, user} = this.props;
        const message = this.state.message;

        return (
            <div>
                <h1>{channel.name}</h1>
                <Messages ref="messages" comments={channel.comments} username={user.username}/>
                <div className="form-group">
                    <label>Message</label>
                    <textarea onKeyDown={this.onMessageBoxKeyDown}
                              onChange={this.onMessageBoxChange}
                              value={message}
                              name="message"
                              className="form-control"
                              placeholder="Message text..."
                              rows="2"/>
                </div>
            </div>
        );
    }
}

Chat.contextTypes = {
    router: PropTypes.object.isRequired
};

const mapStateToProps = (state) => {
    const {channel, error} = state.channel;
    return {
        user: state.account.user,
        channel: channel,
        error: error
    };
};

export default connect(mapStateToProps)(Chat);
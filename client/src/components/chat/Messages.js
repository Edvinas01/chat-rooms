import React, {Component, PropTypes} from 'react';
import ReactDOM from 'react-dom';
import moment from 'moment'
import './messages.css'

const SCROLL_OFFSET = 200;

class Messages extends Component {

    constructor(props) {
        super(props);
        this.scrollToBottom = this.scrollToBottom.bind(this);
    }

    scrollToBottom(force) {
        var box = ReactDOM.findDOMNode(this.refs.chatBox);
        if ((box.scrollTop + box.clientHeight + SCROLL_OFFSET >= box.scrollHeight) || force) {
            box.scrollTop = box.scrollHeight;
        }
    }

    componentDidUpdate(preProps) {
        const {comments} = this.props;

        if (preProps.comments.length !== comments.length) {
            this.scrollToBottom(true);
        }
    }

    render() {
        const {comments, username} = this.props;

        return (
            <div className="chat-box chat-border" ref="chatBox">
                {
                    comments.map((comment, i) => {
                        return (
                            <div key={i}>
                                <div className="message-heading">
                                    <b>{comment.username === username
                                        ? 'You'
                                        : comment.username}</b> said {moment(comment.sentOn).fromNow()}
                                </div>
                                <div className="comment-text">
                                    {comment.message}
                                </div>
                                {
                                    i + 1 < comments.length ? <hr/> : <br/>
                                }
                            </div>
                        );
                    })
                }
            </div>
        );
    }
}

Messages.propTypes = {
    comments: PropTypes.array.isRequired,
    username: PropTypes.string.isRequired
};

export default Messages;
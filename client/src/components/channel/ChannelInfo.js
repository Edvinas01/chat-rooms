import React, {Component, PropTypes} from 'react';
import {Link} from 'react-router';

class ChannelInfo extends Component {

    getCountString(count) {
        if (count === 0) {
            return 'no comments';
        }
        return count + (count === 1 ? ' comment' : ' comments');
    }

    render() {
        const {channel, user, deleteChannel} = this.props;

        return (
            <div className="list-group-item">
                <h4 className="list-group-item-heading">
                    {channel.name}
                </h4>
                <p className="list-group-item-text">
                    This channel has {this.getCountString(channel.commentCount)}.
                    You can enter it by clicking <Link to={`/channels/${channel.id}`}>here</Link>.
                </p>
                {
                    user.admin &&
                    <div>
                        <hr/>
                        <button className="btn btn-sm btn-danger" onClick={() => deleteChannel(channel.id)}>Delete</button>
                    </div>
                }
            </div>
        );
    }
}

ChannelInfo.propTypes = {
    deleteChannel: PropTypes.func.isRequired,
    channel: PropTypes.object.isRequired,
    user: PropTypes.object.isRequired
};

export default ChannelInfo;
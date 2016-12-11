import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router';

import ChannelInfo from '../../components/channel/ChannelInfo';
import CreateModal from '../../components/channel/CreateModal';

import {
    deleteChannel,
    createChannel,
    loadChannels
} from '../../actions/channel';

class Home extends Component {

    constructor(props) {
        super(props);

        this.state = {
            showCreateModal: false
        };

        this.deleteChannel = this.deleteChannel.bind(this);
        this.handleCreateChannel = this.handleCreateChannel.bind(this);
        this.hideCreateModal = this.hideCreateModal.bind(this);
        this.showCreateModal = this.showCreateModal.bind(this);
    }

    componentDidMount() {
        const {user} = this.props;

        if (user) {
            this.props.dispatch(loadChannels());
        }
    }

    componentDidUpdate() {
        const {user, channels} = this.props;
        if (user && channels.length === 0) {
            this.props.dispatch(loadChannels());
        }
    }

    deleteChannel(id) {
        this.props.dispatch(deleteChannel(id));
    }

    handleCreateChannel(name) {
        this.props.dispatch(createChannel(name));
        this.hideCreateModal();
    }

    hideCreateModal() {
        this.setState({
            showCreateModal: false
        });
    }

    showCreateModal() {
        this.setState({
            showCreateModal: true
        });
    }

    render() {
        const {channels, user, error} = this.props;

        const channelOptions = user && channels.map((channel) => {
                return (
                    <ChannelInfo key={channel.id} channel={channel} user={user} deleteChannel={this.deleteChannel}/>
                )
            });

        const rooms = user ? (
            <div>
                <h4>Channels</h4>
                {channelOptions.length === 0 ? 'There are no channels.' : 'Select a channel to join. '}
                <div className="list-group">
                    {channelOptions}
                </div>
                {
                    user.admin &&
                    <span>
                        You can create a new channel by clicking <a href="#" onClick={this.showCreateModal}>here</a>.
                    </span>
                }
            </div>
        ) : (
            <div>
                To access all features, you must first <Link to={'/login'}>login</Link> or <Link
                to={'/register'}>create</Link> a new account.
            </div>
        );

        return (
            <div>
                {
                    error &&
                    <div className="alert alert-danger">
                        {error.message}.
                    </div>
                }
                <h1>Home</h1>
                <p>
                    Welcome to Chat Rooms, the super awesome chat application!
                </p>
                {rooms}

                {
                    this.state.showCreateModal
                        ? <CreateModal handleSubmit={this.handleCreateChannel} handleHide={this.hideCreateModal}/>
                        : null
                }
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        channels: state.channels.channels,
        error: state.channels.error,
        loading: state.account.loading,
        user: state.account.user
    };
};

export default connect(mapStateToProps)(Home);
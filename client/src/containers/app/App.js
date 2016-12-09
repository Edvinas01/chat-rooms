import React, {Component, PropTypes} from 'react';

import {connect} from 'react-redux';
import Sidebar from '../../components/sidebar/Sidebar';
import {loadProfile, logout} from '../../actions/account'

import './app.css';

class App extends Component {

    componentDidMount() {
        // if (this.props.loggedIn && !this.props.user) {
        this.props.dispatch(loadProfile(this.props));
        // }
    }

    componentWillReceiveProps(nextProps) {
        // if (nextProps.loggedIn && !nextProps.loadingProfile && !nextProps.user) {
        this.props.dispatch(loadProfile(nextProps));
        // }
    }

    handleLogout() {
        this.props.dispatch(logout());
        this.context.router.replace('/');
    }

    render() {
        const {user} = this.props;

        const path = this.props.location.pathname;
        const loginOrRegister = path.indexOf('login') > -1 || path.indexOf('register') > -1;

        // If were logging in or registering, do not display the sidebar layout.
        const mainContent = !loginOrRegister ? (
            <div className="row">
                <div className="col-sm-2">
                    <Sidebar location={this.props.location} user={user} handleLogout={() => this.handleLogout()}/>
                </div>
                <div className="col-sm-10">
                    <div className="app-content">
                        {this.props.children}
                    </div>
                </div>
            </div>
        ) : (
            <div className="row">
                <div className="col-sm-12">
                    {this.props.children}
                </div>
            </div>
        );

        return (
            <div className="container-fluid">
                {mainContent}
            </div>
        );
    }
}

App.propTypes = {
    user: PropTypes.object,
    loggedIn: PropTypes.bool,
    children: PropTypes.node.isRequired,
    dispatch: PropTypes.func.isRequired,
    location: PropTypes.object.isRequired
};

App.contextTypes = {
    router: PropTypes.object.isRequired,
    store: PropTypes.object.isRequired
};

const mapStateToProps = (state) => {
    const {account} = state;
    return {
        loadingProfile: account.loading,
        loggedIn: state.login.loggedIn,
        user: account.user
    };
};

export default connect(
    mapStateToProps
)(App);
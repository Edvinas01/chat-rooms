import {Component, PropTypes} from 'react';
import {connect} from 'react-redux';

import {
    loginError
} from '../../actions/account'

class RestrictPage extends Component {

    componentDidUpdate() {
        const {user, loading} = this.props;
        const {router} = this.context;

        // If account is not loading or account is not admin, redirect.
        if ((!loading && !user) || (user && !user.admin)) {
            this.props.dispatch(loginError('Insufficient rights, please login to administrator account'));

            const path = this.props.location.pathname;
            router.push(`/login?redirect=${path}`);
        }
    }

    render() {
        const {user} = this.props;
        if (user) {
            return this.props.children;
        }
        return null;
    }
}

RestrictPage.propTypes = {
    user: PropTypes.object,
    children: PropTypes.object,
    location: PropTypes.object
};

RestrictPage.contextTypes = {
    router: PropTypes.object.isRequired
};

function mapStateToProps(state) {
    return {user: state.account.user, loading: state.account.loading};
}

export default connect(mapStateToProps)(RestrictPage);
import {Component, PropTypes} from 'react';
import {connect} from 'react-redux';

import {
    loginError
} from '../../actions/account'

class RestrictPage extends Component {

    constructor(props) {
        super(props);

        this.redirect = this.redirect.bind(this);
    }

    redirect(home) {
        const {router} = this.context;

        this.props.dispatch(loginError('Insufficient rights, please login to administrator account'));

        const path = this.props.location.pathname;
        if (home) {
            router.push('/login?redirect=/');
        } else {
            router.push(`/login?redirect=${path}`);
        }
    }

    componentDidMount() {
        const {user} = this.props;

        if (user && !user.admin) {
            this.redirect(true);
        }
    }

    componentDidUpdate() {
        const {user, loading} = this.props;

        // If account is not loading or account is not admin, redirect.
        if ((!loading && !user) || (user && !user.admin)) {
            this.redirect();
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
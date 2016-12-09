import React, {Component, PropTypes} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router';

import {login} from '../../actions/account';

import './login.css';

class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: ''
        };

        this.handleCredentialsChange = this.handleCredentialsChange.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.loggedIn) {
            try {
                const redirect = this.props.location.query.redirect;
                this.context.router.replace(redirect);
            } catch (e) {
                this.context.router.replace('/');
            }
        }
    }

    handleLogin(event) {
        event.preventDefault();

        const {username, password} = this.state;
        this.props.dispatch(login(username, password));
    }

    handleCredentialsChange(event) {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    render() {
        const {username, password} = this.state;
        const {error} = this.props;

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-4" style={{ float: 'none', margin: '0 auto' }}>
                        <div className="login-wrapper">
                            {
                                error &&
                                <div className="alert alert-danger">
                                    {error.message}.
                                </div>
                            }
                            <div className="card">
                                <div className="card-header">Login</div>
                                <form className="card-block"
                                      onChange={this.handleCredentialsChange}
                                      onSubmit={this.handleLogin}>
                                    <div className="input-group">
                                        <span className="input-group-addon">
                                            <i className="fa fa-user"/>
                                        </span>
                                        <input type="text"
                                               value={username}
                                               className="form-control"
                                               placeholder="username"
                                               name="username"
                                               autoFocus
                                               required/>
                                    </div>
                                    <div className="input-group">
                                        <span className="input-group-addon">
                                            <i className="fa fa-lock"/>
                                        </span>
                                        <input type="password"
                                               value={password}
                                               className="form-control"
                                               placeholder="password"
                                               name="password"
                                               required/>
                                    </div>
                                    <button type="submit" className="btn btn-primary btn-block">
                                        Login
                                    </button>
                                    <hr/>
                                    <small>
                                        Don't have an account? <Link to={'/register'}>Register</Link>
                                    </small>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

Login.contextTypes = {
    router: PropTypes.object.isRequired,
    store: PropTypes.object.isRequired
};

Login.propTypes = {
    error: PropTypes.object,
    dispatch: PropTypes.func.isRequired,
    location: PropTypes.object
};

function mapStateToProps(state) {
    const {login} = state;

    if (login) {
        return {loggedIn: login.loggedIn, error: login.error};
    }
    return {loggedIn: false};
}

export default connect(mapStateToProps)(Login);
import React, {Component, PropTypes} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router';

import {
    registerFailureMessage,
    registerClear,
    register
} from '../../actions/account';

import './register.css';

class Register extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: '',
            repeatPassword: ''
        };

        this.handleCredentialsChange = this.handleCredentialsChange.bind(this);
        this.handleRegister = this.handleRegister.bind(this);
    }

    componentDidMount() {
        this.props.dispatch(registerClear());
    }

    handleRegister(event) {
        event.preventDefault();
        const {username, password, repeatPassword} = this.state;
        
        if (password !== repeatPassword) {
            this.props.dispatch(registerFailureMessage('Passwords do not match'));
        } else {
            this.props.dispatch(register(username, password));
        }
    }

    handleCredentialsChange(event) {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    render() {
        const {username, password, repeatPassword} = this.state;
        const {error, user} = this.props;

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-4" style={{ float: 'none', margin: '0 auto' }}>
                        <div className="register-wrapper">
                            {
                                !user && error &&
                                <div className="alert alert-danger">
                                    {error.message}.
                                </div>
                            }

                            {
                                user &&
                                <div className="card">
                                    <div className="card-header">
                                        Registered <b>{user.username}</b>
                                    </div>
                                    <div className="card-block">
                                        Please wait until your account is verified by the administrator.
                                        <Link to={'/'}> Home </Link>
                                    </div>
                                </div>
                            }

                            {
                                !user &&
                                <div className="card">
                                    <div className="card-header">Register new account</div>
                                    <form className="card-block"
                                          onChange={this.handleCredentialsChange}
                                          onSubmit={this.handleRegister}>
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
                                        <div className="input-group">
                                        <span className="input-group-addon">
                                            <i className="fa fa-lock"/>
                                        </span>
                                            <input type="password"
                                                   value={repeatPassword}
                                                   className="form-control"
                                                   placeholder="Repeat password"
                                                   name="repeatPassword"
                                                   required/>
                                        </div>
                                        <button type="submit" className="btn btn-primary btn-block">
                                            Register
                                        </button>
                                        <hr/>
                                        <small>
                                            Already have an account? <Link to={'/login'}>Login</Link>
                                        </small>
                                    </form>
                                </div>
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

Register.contextTypes = {
    router: PropTypes.object.isRequired,
    store: PropTypes.object.isRequired
};

Register.propTypes = {
    error: PropTypes.object,
    dispatch: PropTypes.func.isRequired,
    location: PropTypes.object
};

function mapStateToProps(state) {
    const {register} = state;

    console.log(register);
    if (register) {
        return {error: register.error, user: register.user};
    }
    return {error: null, user: null};
}

export default connect(mapStateToProps)(Register);
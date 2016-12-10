import React, {Component} from 'react';
import {connect} from 'react-redux';

import {
    enableUser,
    loadUsers
} from '../../actions/users'

class Users extends Component {

    constructor(props) {
        super(props);
        this.handleEnableUser = this.handleEnableUser.bind(this);
    }

    componentDidMount() {
        this.props.dispatch(loadUsers());
    }

    handleEnableUser(user) {
        this.props.dispatch(enableUser(user.id, !user.enabled));
    }

    render() {
        const {users} = this.props;

        const mapped = users.length === 0 ? (
            <tr>
                <td colSpan="4">No users.</td>
            </tr>
        ) : users.map((user) => {
            return (
                <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.username}</td>
                    <td>{user.role}</td>
                    <td>
                        <button onClick={() => this.handleEnableUser(user)}
                                className={`btn btn-sm ${user.enabled ? 'btn-success' : 'btn-danger'}`}>
                            {user.enabled ? 'Yes' : 'No'}
                        </button>
                    </td>
                </tr>
            )
        });

        return (
            <div>
                <h1>Users</h1>
                <p>
                    Administrate user accounts.
                </p>
                <div>
                    <table className="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th width="40%">Id</th>
                            <th width="25%">Username</th>
                            <th width="25%">Role</th>
                            <th width="10%">Enabled</th>
                        </tr>
                        </thead>
                        <tbody>
                        {mapped}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {users: state.users.users};
};

export default connect(mapStateToProps)(Users);
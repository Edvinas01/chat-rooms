import React, {Component, PropTypes} from 'react';
import {Link, IndexLink} from 'react-router';
import './sidebar.css';

class Sidebar extends Component {
    render() {
        const {user} = this.props;
        const pathname = this.props.location.pathname;

        const home = pathname === '/';
        const about = pathname.indexOf('about') > -1;
        const login = pathname.indexOf('login') > -1;
        const users = pathname.indexOf('users') > -1;

        const userMenu = users;

        const profileDetails = user ? (
            <ul className={`sub-menu collapse ${userMenu ? 'in' : ''}`} id="user">
                {
                    user.admin &&
                    <li>
                        <Link to="/users" className={`nav-link ${users ? 'active': ''}`}>
                            <i className="fa fa-chevron-right fa-sm"/>
                            <i className="fa fa-users fa-lg"/> Users
                        </Link>
                    </li>
                }
                <li>
                    <a className="nav-link" onClick={this.props.handleLogout}>
                        <i className="fa fa-chevron-right fa-sm"/>
                        <i className="fa fa-book fa-lg"/> Logout
                    </a>
                </li>
            </ul>
        ) : (
            <li className={login ? 'active' : ''}>
                <Link to="/login" className="nav-link">
                    <i className="fa fa-book fa-lg"/> Login
                </Link>
            </li>
        );

        return (
            <div className="nav-side-menu">
                <div className="brand">Chat Rooms</div>
                <i className="fa fa-bars fa-2x toggle-btn" data-toggle="collapse" data-target="#menu-content"/>

                <div className="menu-list">
                    <ul id="menu-content" className="menu-content collapse out">
                        <li className={home ? 'active' : ''}>
                            <IndexLink to="/" className="nav-link">
                                <i className="fa fa-home fa-lg"/> Home
                            </IndexLink>
                        </li>

                        <li className={about ? 'active' : ''}>
                            <Link to="/about" className="nav-link">
                                <i className="fa fa-book fa-lg"/> About
                            </Link>
                        </li>

                        {
                            user &&
                            <li data-toggle="collapse" data-target="#user" className={userMenu ? 'out' : 'collapsed'}>
                                <a href="#">
                                    <i className="fa fa-user fa-lg"/> {user.username}
                                    <span className="arrow"/>
                                </a>
                            </li>
                        }
                        {profileDetails}
                    </ul>
                </div>
            </div>
        );
    }
}

Sidebar.propTypes = {
    user: PropTypes.object,
    handleLogout: PropTypes.func.isRequired,
    location: React.PropTypes.object
};

export default Sidebar;
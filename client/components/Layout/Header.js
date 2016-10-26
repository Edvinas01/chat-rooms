import React from 'react';
import Navigation from './Navigation';
import s from './Header.css';
import {IndexLink, Link} from 'react-router';

import {Navbar, Nav, NavItem, NavDropdown, MenuItem} from 'react-bootstrap'

class Header extends React.Component {

    render() {
        return (
            // <header className={`mdl-layout__header ${s.header}`} ref={node => (this.root = node)}>
            //     <div className={`mdl-layout__header-row ${s.row}`}>
            //         <Link className={`mdl-layout-title ${s.title}`} to="/">
            //             React Static Boilerplate
            //         </Link>
            //         <div className="mdl-layout-spacer"></div>
            //         <Navigation />
            //     </div>
            // </header>
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="#">Chat Rooms</a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <li>
                        <IndexLink to="/">Home</IndexLink>
                    </li>
                    <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
                        <MenuItem eventKey={3.1}>Action</MenuItem>
                        <MenuItem eventKey={3.2}>Another action</MenuItem>
                        <MenuItem eventKey={3.3}>Something else here</MenuItem>
                        <MenuItem divider/>
                        <MenuItem eventKey={3.3}>Separated link</MenuItem>
                    </NavDropdown>
                </Nav>
            </Navbar>
        );
    }
}

export default Header;
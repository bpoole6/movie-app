import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Typeahead } from 'react-bootstrap-typeahead';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    useHistory
} from "react-router-dom";
import {Button, Form, FormControl, Modal, Nav, Navbar} from "react-bootstrap";
import {withRouter} from "react-router";
export default function NavbarContainer() {
    const history = useHistory();

    return (<Navbar bg="light" expand="lg">
        <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
                <Nav.Link onClick={() => {
                    localStorage.removeItem("jwt_token")
                    window.location.reload();
                }}>Logout</Nav.Link>
            </Nav>
        </Navbar.Collapse>
    </Navbar>)
}